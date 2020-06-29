package io.github.m1theus.job.service.message;

import io.github.m1theus.job.entity.customer.CustomerEntity;
import io.github.m1theus.job.repository.customer.CustomerRepository;
import io.github.m1theus.job.repository.message.CustomerPendingMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

import static io.github.m1theus.job.async.TaskExecutorConfig.DEFAULT_CORE_TASK_EXECUTOR;
import static io.github.m1theus.job.mapper.CustomerMapper.map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessPendingMessageJobService {

    private final CustomerRepository customerRepository;
    private final CustomerPendingMessageRepository pendingMessageRepository;

    @Value("${job.process.limit}")
    private BigInteger processLimit = BigInteger.ZERO;
    @Value("${job.process.page-size}")
    private Integer pageSize = 1;
    private BigInteger processedCounter = BigInteger.ZERO;

    @Async(DEFAULT_CORE_TASK_EXECUTOR)
    public void start() {
        log.info("c=MessagePublisherService, m=start, status=STARTED");
        Pageable pageable = PageRequest.of(0, pageSize);


        do {
            pageable = pageable.next();
            final var pageableEntities = customerRepository.findAll(pageable);

            final var pendingEntities = pageableEntities.getContent();

            if (pendingEntities.size() == 0) {
                break;
            }

            pendingEntities.parallelStream().forEach(this::savePendingMessage);
        } while (processedCounter.compareTo(processLimit) < 0);
    }

    @Transactional
    public void savePendingMessage(final CustomerEntity customer) {
        pendingMessageRepository.save(map(customer));
        processedCounter = processedCounter.add(BigInteger.ONE);
        log.info("c=MessagePublisherService, m=savePendingMessage, status=SAVED, count={}", processedCounter);
    }
}
