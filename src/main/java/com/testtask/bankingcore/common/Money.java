package com.testtask.bankingcore.common;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record Money(
    BigDecimal amount,
    String currency
) {}
