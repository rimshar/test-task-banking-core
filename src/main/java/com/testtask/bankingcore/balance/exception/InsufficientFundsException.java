package com.testtask.bankingcore.balance.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InsufficientFundsException extends ResponseStatusException {

    public InsufficientFundsException() {
        super(HttpStatus.BAD_REQUEST, "Insufficient funds");
    }
}
