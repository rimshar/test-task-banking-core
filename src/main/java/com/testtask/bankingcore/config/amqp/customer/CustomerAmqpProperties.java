package com.testtask.bankingcore.config.amqp.customer;

import com.testtask.bankingcore.common.notification.AmqpProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "customer")
public record CustomerAmqpProperties(Creation creation) {
    public record Creation(
        AmqpProperties amqp
    ) {}
}
