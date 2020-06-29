package io.github.m1theus.job.repository.message;

import io.github.m1theus.job.entity.message.CustomerPublishedMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerPublishedMessageRepository extends JpaRepository<CustomerPublishedMessageEntity, Long> {
}
