package com.testtask.bankingcore.currency.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidCurrencyException extends ResponseStatusException {

    public InvalidCurrencyException(String currencyCode) {
        super(HttpStatus.BAD_REQUEST, "Invalid currency: " + currencyCode);
    }
}
