package io.github.m1theus.job.service.customer;

import com.github.javafaker.Faker;
import io.github.m1theus.job.entity.customer.CustomerEntity;
import io.github.m1theus.job.repository.customer.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final Faker fakerService;
    private final CustomerRepository customerRepository;

    public CustomerEntity createCustomer() {
        return CustomerEntity.builder()
                .firstName(fakerService.name().firstName())
                .lastName(fakerService.name().lastName())
                .email(fakerService.internet().emailAddress())
                .nationality(fakerService.nation().nationality())
                .country(fakerService.address().country())
                .fullAddress(fakerService.address().fullAddress())
                .build();
    }

    @Transactional
    public void saveCustomer(final CustomerEntity customer) {
        customerRepository.save(customer);
    }
}
