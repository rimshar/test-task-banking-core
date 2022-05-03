package com.testtask.bankingcore.transaction.notification;

import com.testtask.bankingcore.transaction.enums.TransactionDirection;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record TransactionCreationNotificationMessage(
    Long transactionId,
    BigDecimal amount,
    String currency,
    TransactionDirection direction,
    String description
) {}
