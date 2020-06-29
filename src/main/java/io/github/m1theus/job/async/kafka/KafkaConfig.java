package io.github.m1theus.job.async.kafka;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

import static io.confluent.kafka.serializers.KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.MAX_BLOCK_MS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.RETRIES_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG;

@EnableKafka
@Configuration
public class KafkaConfig {

    private final KafkaProperties properties;

    private static final int MAX_PUBLISH_ATTEMPTS = Integer.MAX_VALUE;
    private static final int MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION = 1;
    private static final int RETRY_BACKOFF_MS_CONFIG = 3_000;

    @Value("${kafka.bootstrap-servers}")
    private String kafkaBootstrapServers;

    @Value("${kafka.schema-registry-url}")
    private String schemaRegistryURL;

    @Value("${kafka.request.timeout}")
    private Integer kafkaMaxMillisTimeout;

    public KafkaConfig(final KafkaProperties properties) {
        this.properties = properties;
    }

    @Bean
    public <T1, T2> ProducerFactory<T1, T2> producerFactory() {
        final Map<String, Object> configProps = properties.buildProducerProperties();
        configProps.put(REQUEST_TIMEOUT_MS_CONFIG, kafkaMaxMillisTimeout);
        configProps.put(BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
        configProps.put(MAX_BLOCK_MS_CONFIG, kafkaMaxMillisTimeout);
        configProps.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);
        configProps.put(SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryURL);

        // Only one in-flight messages per Kafka broker connection
        configProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION);
        configProps.put(RETRIES_CONFIG, MAX_PUBLISH_ATTEMPTS);

        // Only retry after three seconds.
        configProps.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, RETRY_BACKOFF_MS_CONFIG);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public <T1, T2> KafkaTemplate<T1, T2> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
