package com.testtask.bankingcore.config.amqp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Validated
@ConfigurationProperties(prefix = "amqp")
public record AmqpProperties(
    @NotEmpty List<AmqpExchange> exchanges,
    @NotEmpty List<AmqpQueue> queues
) {

    public record AmqpExchange(
        @NotBlank String name
    ) {}

    public record AmqpQueue(
        @NotBlank String name,
        AmqpQueueBinding boundTo
    ) {

        record AmqpQueueBinding(
            @NotBlank String exchange,
            @NotBlank String routingKey
        ) {}
    }
}
