package io.github.m1theus.job.repository.message;

import io.github.m1theus.job.entity.message.CustomerPendingMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerPendingMessageRepository extends JpaRepository<CustomerPendingMessageEntity, Long> {
}
