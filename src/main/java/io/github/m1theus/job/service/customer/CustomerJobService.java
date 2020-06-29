package io.github.m1theus.job.service.customer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

import static io.github.m1theus.job.async.TaskExecutorConfig.DEFAULT_CORE_TASK_EXECUTOR;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerJobService {

    private final CustomerService customerService;

    @Value("${job.generate.limit}")
    private BigInteger generateLimit = BigInteger.ZERO;

    private BigInteger processedCounter = BigInteger.ZERO;

    @Async(DEFAULT_CORE_TASK_EXECUTOR)
    public void start() {
        log.info("c=CustomerJobService, m=start, status=STARTED");

        do {
            final var customer = customerService.createCustomer();

            customerService.saveCustomer(customer);
            processedCounter = processedCounter.add(BigInteger.ONE);
            log.info("c=CustomerJobService, M=start, status=SAVED, counter={}", processedCounter);
        } while (processedCounter.compareTo(generateLimit) < 0);

        log.info("c=CustomerJobService, M=start, status=FINISHED, counter={}", processedCounter);
    }

}
