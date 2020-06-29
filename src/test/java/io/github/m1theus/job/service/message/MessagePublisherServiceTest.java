package io.github.m1theus.job.service.message;

import io.github.m1theus.CustomerMessage;
import io.github.m1theus.job.entity.message.CustomerPendingMessageEntity;
import io.github.m1theus.job.repository.message.CustomerPendingMessageRepository;
import io.github.m1theus.job.repository.message.CustomerPublishedMessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;

import static io.github.m1theus.job.helper.CustomerHelper.mockCustomerPendingMessage;
import static io.github.m1theus.job.mapper.CustomerMapper.map;
import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class MessagePublisherServiceTest {

    @Mock
    CustomerPendingMessageRepository pendingMessageRepository;

    @Mock
    CustomerPublishedMessageRepository publishedMessageRepository;

    @Mock
    KafkaTemplate<String, CustomerMessage> kafkaTemplate;

    MessagePublisherService publisher;

    @BeforeEach
    public void setUp() {
        initMocks(this);
        publisher = new MessagePublisherService(pendingMessageRepository, publishedMessageRepository, kafkaTemplate);
    }

    @Test
    public void should_be_fetch_pending_messages_and_publish_and_save() {
        // given
        mockPendingMessages();
        ListenableFuture<SendResult<String, CustomerMessage>> listenableFuture = mock(ListenableFuture.class);
        when(kafkaTemplate.send(any(), any(), any(CustomerMessage.class))).thenReturn(listenableFuture);
        doAnswer(invocationOnMock -> {
            ListenableFutureCallback listenableFutureCallback = invocationOnMock.getArgument(0);
            listenableFutureCallback.onSuccess(any(SendResult.class));
            return any();
        }).when(listenableFuture).addCallback(any(ListenableFutureCallback.class));

        // when
        publisher.start();

        // then
        verify(pendingMessageRepository).findAll(any(Pageable.class));
        verify(kafkaTemplate).send(anyString(), any(), any());
        // TODO - mock kafka success result
        // verify(publishedMessageRepository).save(any());
        // verify(pendingMessageRepository).delete(any());
    }

    @Test
    public void should_be_not_save_when_publish_failed() {
        // given
        final var pendingMessage = mockCustomerPendingMessage();
        final var message = "ANYTHING";
        mockPendingMessages();
        ListenableFuture<SendResult<String, CustomerMessage>> responseFuture = mock(ListenableFuture.class);
        Throwable throwable = mock(Throwable.class);
        given(throwable.getMessage()).willReturn(message);
        when(kafkaTemplate.send(any(), any(), any())).thenReturn(responseFuture);
        doAnswer(invocationOnMock -> {
            ListenableFutureCallback listenableFutureCallback = invocationOnMock.getArgument(0);
            listenableFutureCallback.onFailure(throwable);
            return null;
        }).when(responseFuture).addCallback(any(ListenableFutureCallback.class));

        // when
        publisher.start();

        // then
        verify(publishedMessageRepository, never()).save(map(pendingMessage));
        verify(pendingMessageRepository, never()).delete(pendingMessage);
    }

    @Test
    public void should_break_publisher_when_not_have_pending_messages() {
        // given
        mockEmptyPendingMessages();

        // when
        publisher.start();

        // then
        verify(pendingMessageRepository).findAll(any(Pageable.class));
        verifyNoMoreInteractions(pendingMessageRepository);
    }

    private void mockEmptyPendingMessages() {
        Page<CustomerPendingMessageEntity> page = mock(Page.class);
        when(page.getContent()).thenReturn(emptyList());
        when(page.getTotalElements()).thenReturn(0L);
        when(pendingMessageRepository.findAll(any(Pageable.class))).thenReturn(page);
    }

    private void mockPendingMessages() {
        Page<CustomerPendingMessageEntity> page = mock(Page.class);
        when(page.getContent()).thenReturn(List.of(mockCustomerPendingMessage()));
        when(page.getTotalElements()).thenReturn(1L);
        when(pendingMessageRepository.findAll(any(Pageable.class))).thenReturn(page);
    }

}