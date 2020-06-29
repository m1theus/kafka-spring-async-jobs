package io.github.m1theus.job.mapper;

import io.github.m1theus.CustomerMessage;
import io.github.m1theus.job.entity.customer.CustomerEntity;
import io.github.m1theus.job.entity.message.CustomerPendingMessageEntity;
import io.github.m1theus.job.entity.message.CustomerPublishedMessageEntity;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class CustomerMapper {

    public static CustomerPendingMessageEntity map(final CustomerEntity customer) {
        return CustomerPendingMessageEntity.builder()
                .customerId(customer.getId())
                .fullName(customer.getFullName())
                .email(customer.getEmail())
                .nationality(customer.getNationality())
                .country(customer.getCountry())
                .fullAddress(customer.getFullAddress())
                .build();
    }

    public static CustomerPublishedMessageEntity map(final CustomerPendingMessageEntity pendingMessage) {
        return CustomerPublishedMessageEntity.builder()
                .id(pendingMessage.getId())
                .customerId(pendingMessage.getCustomerId())
                .fullName(pendingMessage.getFullName())
                .email(pendingMessage.getEmail())
                .nationality(pendingMessage.getNationality())
                .country(pendingMessage.getCountry())
                .fullAddress(pendingMessage.getFullAddress())
                .build();
    }

    public static CustomerMessage toMessage(final CustomerPendingMessageEntity pendingMessage) {
        return CustomerMessage.newBuilder()
                .setID(pendingMessage.getId())
                .setCUSTOMERID(pendingMessage.getCustomerId())
                .setEMAIL(pendingMessage.getEmail())
                .setFULLNAME(pendingMessage.getFullName())
                .setNATIONALITY(pendingMessage.getNationality())
                .setCOUNTRY(pendingMessage.getCountry())
                .setFULLADDRESS(pendingMessage.getFullAddress())
                .build();
    }

}
