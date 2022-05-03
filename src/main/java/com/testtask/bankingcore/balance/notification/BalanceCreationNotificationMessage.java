package com.testtask.bankingcore.balance.notification;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record BalanceCreationNotificationMessage(
    Long balanceId,
    Long accountId,
    BigDecimal amount,
    String currency
) {}
