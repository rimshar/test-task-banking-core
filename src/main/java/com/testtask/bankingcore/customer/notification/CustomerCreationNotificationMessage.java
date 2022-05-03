package com.testtask.bankingcore.customer.notification;

import lombok.Builder;

@Builder
public record CustomerCreationNotificationMessage(
    Long customerId,
    String name
) {}
