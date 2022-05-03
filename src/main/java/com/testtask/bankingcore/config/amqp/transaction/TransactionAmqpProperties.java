package com.testtask.bankingcore.config.amqp.transaction;

import com.testtask.bankingcore.common.notification.AmqpProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "transaction")
public record TransactionAmqpProperties(Creation creation) {
    public record Creation(
        AmqpProperties amqp
    ) {}
}
