package com.testtask.bankingcore.customer;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerMapper mapper;

    public Long createCustomer(String name) {
        val customer = CustomerRecord.builder().name(name).build();
        mapper.save(customer);
        return customer.getId();
    }
}
