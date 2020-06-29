package io.github.m1theus.job.config;

import com.github.javafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
public class BeanConfig {

    @Bean
    public Faker getFaker() {
        return Faker.instance(Locale.getDefault());
    }

}
