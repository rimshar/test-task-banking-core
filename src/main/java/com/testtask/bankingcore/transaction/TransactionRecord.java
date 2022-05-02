package com.testtask.bankingcore.transaction;

import com.testtask.bankingcore.transaction.enums.TransactionDirection;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class TransactionRecord {
    private Long id;
    private Long accountId;
    private BigDecimal amount;
    private Long currencyId;
    private TransactionDirection direction;
    private String description;
}
