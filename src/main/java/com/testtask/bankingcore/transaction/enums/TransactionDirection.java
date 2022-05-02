package com.testtask.bankingcore.transaction.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum TransactionDirection {
    IN, OUT;

    @JsonValue
    public String transactionDirectionValue() {
        return name().toLowerCase();
    }
}
