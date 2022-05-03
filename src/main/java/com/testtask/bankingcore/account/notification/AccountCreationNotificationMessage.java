package com.testtask.bankingcore.account.notification;

import lombok.Builder;

@Builder
public record AccountCreationNotificationMessage(
    Long accountId,
    Long customerId,
    String countryCode
) {}
