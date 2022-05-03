package com.testtask.bankingcore.customer.notification;

import com.testtask.bankingcore.config.amqp.RabbitJson;
import com.testtask.bankingcore.config.amqp.customer.CustomerAmqpProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomerNotificationRabbitClient {

    private final CustomerAmqpProperties properties;
    @RabbitJson
    private final RabbitTemplate rabbit;

    public void sendCreationNotification(CustomerCreationNotificationMessage message) {
        rabbit.convertAndSend(
            properties.creation().amqp().exchange(),
            properties.creation().amqp().routingKey(),
            message
        );
    }
}
