package io.github.m1theus.job.service.customer;

import com.github.javafaker.Faker;
import io.github.m1theus.job.repository.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class CustomerServiceTest {

    CustomerService service;

    @Mock
    CustomerRepository customerRepository;

    @BeforeEach
    public void setUp() {
        initMocks(this);
        service = new CustomerService(Faker.instance(Locale.getDefault()), customerRepository);
    }

    @Test
    public void should_be_create_new_customer_from_faker() {
        // when
        final var customer = service.createCustomer();

        // then
        assertThat(customer).isNotNull();
        assertThat(customer.getFirstName()).isNotNull();
        assertThat(customer.getLastName()).isNotNull();
        assertThat(customer.getEmail()).isNotNull();
        assertThat(customer.getNationality()).isNotNull();
        assertThat(customer.getCountry()).isNotNull();
        assertThat(customer.getFullAddress()).isNotNull();
    }

    @Test
    public void should_be_a_valid_customer() {
        // given
        final var customer = service.createCustomer();

        // then
        assertThat(customer).isNotNull();
        assertThat(customer).hasNoNullFieldsOrPropertiesExcept("id");
        assertThat(customer.getFullAddress()).isNotNull();
    }

    @Test
    public void should_be_save_a_customer() {
        // given
        final var customer = service.createCustomer();

        // when
        service.saveCustomer(customer);

        // then
        verify(customerRepository).save(customer);
    }

}
