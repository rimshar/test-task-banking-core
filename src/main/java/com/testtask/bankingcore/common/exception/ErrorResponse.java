package com.testtask.bankingcore.common.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class ErrorResponse {
    private final String message;
    private final List<String> details;

    public ErrorResponse(String message, List<String> details) {
        super();
        this.message = message;
        this.details = details;
    }
}
