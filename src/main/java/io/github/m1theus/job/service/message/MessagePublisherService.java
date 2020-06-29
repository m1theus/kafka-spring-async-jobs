package io.github.m1theus.job.service.message;

import io.github.m1theus.CustomerMessage;
import io.github.m1theus.job.entity.message.CustomerPendingMessageEntity;
import io.github.m1theus.job.repository.message.CustomerPendingMessageRepository;
import io.github.m1theus.job.repository.message.CustomerPublishedMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

import static io.github.m1theus.job.async.TaskExecutorConfig.DEFAULT_CORE_TASK_EXECUTOR;
import static io.github.m1theus.job.async.kafka.KafkaTopics.CUSTOMER_TOPIC;
import static io.github.m1theus.job.mapper.CustomerMapper.map;
import static io.github.m1theus.job.mapper.CustomerMapper.toMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessagePublisherService {

    private final CustomerPendingMessageRepository pendingMessageRepository;
    private final CustomerPublishedMessageRepository publishedMessageRepository;
    private final KafkaTemplate<String, CustomerMessage> kafkaTemplate;

    @Value("${job.process.limit}")
    private BigInteger processLimit = BigInteger.ONE;
    @Value("${job.process.page-size}")
    private Integer pageSize = 1;
    private BigInteger processedCounter = BigInteger.ZERO;

    @Async(DEFAULT_CORE_TASK_EXECUTOR)
    public void start() {
        log.info("c=MessagePublisherService, m=start, status=STARTED");
        Pageable pageable = PageRequest.of(0, pageSize);


        do {
            pageable = pageable.next();
            final var pageableEntities = pendingMessageRepository.findAll(pageable);

            final var pendingEntities = pageableEntities.getContent();

            if (pendingEntities.size() == 0) {
                break;
            }

            pendingEntities.parallelStream().forEach(this::publishAndSave);
        } while (processedCounter.compareTo(processLimit) < 0);
    }

    private void publishAndSave(final CustomerPendingMessageEntity pendingMessage) {
        publish(pendingMessage);
        processedCounter = processedCounter.add(BigInteger.ONE);
    }

    public void publish(final CustomerPendingMessageEntity pendingMessage) {
        final var message = toMessage(pendingMessage);
        kafkaTemplate.send(CUSTOMER_TOPIC, message.getID().toString(), message).addCallback(
                result -> savePublishedAndDeletePending(pendingMessage),
                error -> log.error("M=publishMessage messageId={} topic={} Event Error", message.getID(), CUSTOMER_TOPIC, error)
        );
    }

    private void savePublishedAndDeletePending(final CustomerPendingMessageEntity pendingMessage) {
        publishedMessageRepository.save(map(pendingMessage));
        pendingMessageRepository.delete(pendingMessage);
        log.info("M=publishMessage messageId={} topic={} Event Published Successfully", pendingMessage.getId(), CUSTOMER_TOPIC);
    }

}
