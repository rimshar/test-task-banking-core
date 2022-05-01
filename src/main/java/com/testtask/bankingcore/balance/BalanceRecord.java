package com.testtask.bankingcore.balance;

import com.testtask.bankingcore.balance.api.v1.BalanceResponse;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record BalanceRecord(
    String id,
    String accountId,
    BigDecimal amount,
    String currencyId
) {}
