package com.testtask.bankingcore.balance.api.v1;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record BalanceResponse(
    BigDecimal availableAmount,
    String currency
) {}
