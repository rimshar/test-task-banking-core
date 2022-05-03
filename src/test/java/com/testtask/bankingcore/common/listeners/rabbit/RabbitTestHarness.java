package com.testtask.bankingcore.common.listeners.rabbit;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.DirectMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitTestHarness {

    private final AmqpAdmin admin;
    private final ConnectionFactory connectionFactory;

    public RabbitQueueMessageListener listen(String exchange, String topic) {
        return listen((Exchange) ExchangeBuilder.topicExchange(exchange).build(), topic);
    }

    private RabbitQueueMessageListener listen(Exchange exchange, String topic) {
        val queue = admin.declareQueue();
        admin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(topic).noargs());
        val listener = new RabbitQueueMessageListener();
        val container = new DirectMessageListenerContainer(connectionFactory);
        container.setMessageListener(listener);
        container.addQueues(queue);
        container.start();
        return listener;
    }
}
