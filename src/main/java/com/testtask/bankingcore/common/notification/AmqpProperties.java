package com.testtask.bankingcore.common.notification;

public record AmqpProperties(
    String exchange,
    String routingKey
) {}
