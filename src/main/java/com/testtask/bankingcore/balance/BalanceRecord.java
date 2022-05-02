package com.testtask.bankingcore.balance;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class BalanceRecord {
    private Long id;
    private Long accountId;
    private BigDecimal amount;
    private Long currencyId;
}
