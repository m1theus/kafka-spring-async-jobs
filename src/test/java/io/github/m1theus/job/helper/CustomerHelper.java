package io.github.m1theus.job.helper;

import io.github.m1theus.job.entity.customer.CustomerEntity;
import io.github.m1theus.job.entity.message.CustomerPendingMessageEntity;

public class CustomerHelper {

    public static CustomerEntity mockCustomer() {
        return CustomerEntity.builder()
                .id(666L)
                .firstName("Matheus")
                .lastName("Martins")
                .email("m1theus@github.io")
                .nationality("Brazilian")
                .country("Brazil")
                .fullAddress("Brazzzzzzzzzzzzzil")
                .build();
    }

    public static CustomerPendingMessageEntity mockCustomerPendingMessage() {
        return CustomerPendingMessageEntity.builder()
                .id(999L)
                .customerId(mockCustomer().getId())
                .fullName(mockCustomer().getFullName())
                .email(mockCustomer().getEmail())
                .nationality(mockCustomer().getNationality())
                .country(mockCustomer().getCountry())
                .fullAddress(mockCustomer().getFullAddress())
                .build();
    }

}
