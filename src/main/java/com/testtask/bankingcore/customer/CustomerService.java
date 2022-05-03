package com.testtask.bankingcore.customer;

import com.testtask.bankingcore.customer.notification.CustomerCreationNotificationMessage;
import com.testtask.bankingcore.customer.notification.CustomerNotificationRabbitClient;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerMapper mapper;
    private final CustomerNotificationRabbitClient rabbitClient;

    public Long createCustomer(String name) {
        val customer = CustomerRecord.builder().name(name).build();

        mapper.save(customer);

        rabbitClient.sendCreationNotification(
            CustomerCreationNotificationMessage.builder()
                .customerId(customer.getId())
                .name(name)
                .build()
        );

        return customer.getId();
    }
}
