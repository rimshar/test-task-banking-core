package com.testtask.bankingcore.customer;

import com.testtask.bankingcore.customer.notification.CustomerCreationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/customers", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerController {

    private final CustomerService service;

    @PostMapping
    Long createCustomer(@NotNull @RequestBody CustomerCreationRequest request) {
        return service.createCustomer(request.customerName());
    }
}
