package com.testtask.bankingcore.account.notification;

import com.testtask.bankingcore.config.amqp.RabbitJson;
import com.testtask.bankingcore.config.amqp.account.AccountAmqpProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountNotificationRabbitClient {

    private final AccountAmqpProperties properties;
    @RabbitJson
    private final RabbitTemplate rabbit;

    public void sendCreationNotification(AccountCreationNotificationMessage message) {
        rabbit.convertAndSend(
            properties.creation().amqp().exchange(),
            properties.creation().amqp().routingKey(),
            message
        );
    }
}
