package com.testtask.bankingcore.config.amqp.balance;

import com.testtask.bankingcore.common.notification.AmqpProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "balance")
public record BalanceAmqpProperties(
    Creation creation,
    Update update
) {
    public record Creation(
        AmqpProperties amqp
    ) {}

    public record Update(
        AmqpProperties amqp
    ) {}
}
