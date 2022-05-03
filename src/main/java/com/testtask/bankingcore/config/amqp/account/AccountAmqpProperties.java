package com.testtask.bankingcore.config.amqp.account;

import com.testtask.bankingcore.common.notification.AmqpProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "account")
public record AccountAmqpProperties(Creation creation) {
    public record Creation(
        AmqpProperties amqp
    ) {}
}
