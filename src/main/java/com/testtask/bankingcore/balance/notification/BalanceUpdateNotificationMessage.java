package com.testtask.bankingcore.balance.notification;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record BalanceUpdateNotificationMessage(
    Long accountId,
    Long balanceId,
    String currency,
    BigDecimal updatedBalance
) {}
