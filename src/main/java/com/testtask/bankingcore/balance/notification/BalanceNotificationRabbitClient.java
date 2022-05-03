package com.testtask.bankingcore.balance.notification;
import com.testtask.bankingcore.config.amqp.RabbitJson;
import com.testtask.bankingcore.config.amqp.balance.BalanceAmqpProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BalanceNotificationRabbitClient {

    private final BalanceAmqpProperties properties;
    @RabbitJson
    private final RabbitTemplate rabbit;

    public void sendCreationNotification(BalanceCreationNotificationMessage message) {
        rabbit.convertAndSend(
            properties.creation().amqp().exchange(),
            properties.creation().amqp().routingKey(),
            message
        );
    }

    public void sendUpdateNotification(BalanceUpdateNotificationMessage message) {
        rabbit.convertAndSend(
            properties.update().amqp().exchange(),
            properties.update().amqp().routingKey(),
            message
        );
    }
}
