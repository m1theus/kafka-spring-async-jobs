package io.github.m1theus.job.mapper;

import io.github.m1theus.CustomerMessage;
import org.junit.jupiter.api.Test;

import static io.github.m1theus.job.helper.CustomerHelper.mockCustomer;
import static io.github.m1theus.job.helper.CustomerHelper.mockCustomerPendingMessage;
import static io.github.m1theus.job.mapper.CustomerMapper.map;
import static io.github.m1theus.job.mapper.CustomerMapper.toMessage;
import static org.assertj.core.api.Assertions.assertThat;

public class MapperTest {

    @Test
    public void should_be_map_customer_to_pending_message() {
        // given
        final var customer = mockCustomer();

        // when
        final var pendingMessage = map(customer);

        // then
        assertThat(pendingMessage).isNotNull();
        assertThat(pendingMessage.getId()).isNull();
        assertThat(pendingMessage.getCustomerId()).isEqualTo(customer.getId());
        assertThat(pendingMessage.getFullName()).isEqualTo(customer.getFullName());
        assertThat(pendingMessage.getEmail()).isEqualTo(customer.getEmail());
        assertThat(pendingMessage.getNationality()).isEqualTo(customer.getNationality());
        assertThat(pendingMessage.getCountry()).isEqualTo(customer.getCountry());
        assertThat(pendingMessage.getFullAddress()).isEqualTo(customer.getFullAddress());
    }

    @Test
    public void should_be_map_pending_message_to_published_message() {
        // given
        final var pendingMessage = mockCustomerPendingMessage();

        // when
        final var publishedMessage = map(pendingMessage);

        // then
        assertThat(publishedMessage).isNotNull();
        assertThat(publishedMessage).isEqualToComparingFieldByField(pendingMessage);
    }

    @Test
    public void should_be_map_pending_message_to_customer_message() {
        // given
        final var pendingMessage = mockCustomerPendingMessage();

        // when
        final var customerMessage = toMessage(pendingMessage);

        // then
        assertThat(customerMessage).isNotNull();
        assertThat(customerMessage.getID()).isEqualTo(pendingMessage.getId());
        assertThat(customerMessage.getCUSTOMERID()).isEqualTo(pendingMessage.getCustomerId());
        assertThat(customerMessage.getFULLNAME()).isEqualTo(pendingMessage.getFullName());
        assertThat(customerMessage.getEMAIL()).isEqualTo(pendingMessage.getEmail());
        assertThat(customerMessage.getNATIONALITY()).isEqualTo(pendingMessage.getNationality());
        assertThat(customerMessage.getCOUNTRY()).isEqualTo(pendingMessage.getCountry());
        assertThat(customerMessage.getFULLADDRESS()).isEqualTo(pendingMessage.getFullAddress());
    }

}
