package io.github.m1theus.job;

import io.github.m1theus.job.service.customer.CustomerJobService;
import io.github.m1theus.job.service.message.MessagePublisherService;
import io.github.m1theus.job.service.message.ProcessPendingMessageJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaApplication implements CommandLineRunner {

    @Autowired
    private CustomerJobService customerJobService;

    @Autowired
    private ProcessPendingMessageJobService processPendingMessageJobService;

    @Autowired
    private MessagePublisherService messagePublisherService;

    public static void main(String[] args) {
        SpringApplication.run(KafkaApplication.class, args);
    }

    @Override
    public void run(String... args) throws InterruptedException {
        Thread t1 = new Thread(customerJobService::start);
        Thread t2 = new Thread(processPendingMessageJobService::start);
        Thread t3 = new Thread(messagePublisherService::start);

        t1.run();
        t2.run();
        t3.run();

        t1.join();
        t2.join();
        t3.join();
    }
}
