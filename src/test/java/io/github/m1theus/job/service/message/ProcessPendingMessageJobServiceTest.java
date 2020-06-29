package io.github.m1theus.job.service.message;

import io.github.m1theus.job.entity.customer.CustomerEntity;
import io.github.m1theus.job.entity.message.CustomerPendingMessageEntity;
import io.github.m1theus.job.repository.customer.CustomerRepository;
import io.github.m1theus.job.repository.message.CustomerPendingMessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ProcessPendingMessageJobServiceTest {

    @Mock
    CustomerRepository customerRepository;

    @Mock
    CustomerPendingMessageRepository pendingMessageRepository;

    ProcessPendingMessageJobService jobService;

    @BeforeEach
    public void setUp() {
        initMocks(this);
        jobService = new ProcessPendingMessageJobService(customerRepository, pendingMessageRepository);
    }

    @Test
    public void should_be_fetch_a_customer_and_save_pending_messages() {
        // given
        mockPendingMessages();

        // when
        jobService.start();

        // then
        verify(customerRepository, only()).findAll(any(Pageable.class));
        verify(pendingMessageRepository, only()).save(any(CustomerPendingMessageEntity.class));
    }

    @Test
    public void should_break_job_when_not_have_pending_messages() {
        // given
        mockEmptyPendingMessages();

        // when
        jobService.start();

        // then
        verify(customerRepository).findAll(any(Pageable.class));
        verifyNoMoreInteractions(pendingMessageRepository);
    }

    private void mockEmptyPendingMessages() {
        Page<CustomerEntity> page = mock(Page.class);
        when(page.getContent()).thenReturn(emptyList());
        when(page.getTotalElements()).thenReturn(0L);
        when(customerRepository.findAll(any(Pageable.class))).thenReturn(page);
    }

    private void mockPendingMessages() {
        Page<CustomerEntity> page = mock(Page.class);
        when(page.getContent()).thenReturn(List.of(mock(CustomerEntity.class)));
        when(page.getTotalElements()).thenReturn(1L);
        when(customerRepository.findAll(any(Pageable.class))).thenReturn(page);
    }

}
