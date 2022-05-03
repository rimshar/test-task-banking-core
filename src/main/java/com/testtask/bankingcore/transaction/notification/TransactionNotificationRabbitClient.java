package com.testtask.bankingcore.transaction.notification;

import com.testtask.bankingcore.config.amqp.RabbitJson;
import com.testtask.bankingcore.config.amqp.transaction.TransactionAmqpProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionNotificationRabbitClient {

    private final TransactionAmqpProperties properties;
    @RabbitJson
    private final RabbitTemplate rabbit;

    public void sendCreationNotification(TransactionCreationNotificationMessage message) {
        rabbit.convertAndSend(
            properties.creation().amqp().exchange(),
            properties.creation().amqp().routingKey(),
            message
        );
    }
}
