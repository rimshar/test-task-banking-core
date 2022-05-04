package com.testtask.bankingcore.transaction.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import java.math.BigDecimal;

public enum TransactionDirection {
    IN {
        @Override
        public BigDecimal calculateAmount(BigDecimal balance, BigDecimal amount) {
            return balance.add(amount);
        }
    },
    OUT{
        @Override
        public BigDecimal calculateAmount(BigDecimal balance, BigDecimal amount) {
            return balance.subtract(amount);
        }
    };

    @JsonValue
    public String transactionDirectionValue() {
        return name().toLowerCase();
    }

    public abstract BigDecimal calculateAmount(BigDecimal balance, BigDecimal amount);
}
