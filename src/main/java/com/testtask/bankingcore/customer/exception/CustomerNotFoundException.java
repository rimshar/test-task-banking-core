package com.testtask.bankingcore.customer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CustomerNotFoundException extends ResponseStatusException {

    public CustomerNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Customer not found");
    }
}
