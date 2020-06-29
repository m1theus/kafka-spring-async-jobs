package io.github.m1theus.job.service.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class CustomerJobServiceTest {

    @Mock
    CustomerService customerService;

    CustomerJobService jobService;

    @BeforeEach
    public void setUp() {
        initMocks(this);
        jobService = new CustomerJobService(customerService);
    }

    @Test
    public void should_be_generate_and_save_a_customer() {
        // when
        jobService.start();

        // then
        verify(customerService).createCustomer();
        verify(customerService).saveCustomer(any());
    }

}
