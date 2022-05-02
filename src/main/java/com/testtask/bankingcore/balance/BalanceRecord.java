package com.testtask.bankingcore.balance;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BalanceRecord {
    private Long id;
    private Long accountId;
    private BigDecimal amount;
    private Long currencyId;
}
