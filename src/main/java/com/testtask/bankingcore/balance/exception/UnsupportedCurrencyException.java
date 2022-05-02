package com.testtask.bankingcore.balance.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UnsupportedCurrencyException extends ResponseStatusException {

    public UnsupportedCurrencyException(Long accountId, String currency) {
        super(HttpStatus.NOT_FOUND, "Account " + accountId + " does not support currency " + currency);
    }
}
