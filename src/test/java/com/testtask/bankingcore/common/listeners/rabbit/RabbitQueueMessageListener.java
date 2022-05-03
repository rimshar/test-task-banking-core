package com.testtask.bankingcore.common.listeners.rabbit;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class RabbitQueueMessageListener implements MessageListener {

    LinkedBlockingQueue<Message> messages = new LinkedBlockingQueue<>();

    @Override
    public void onMessage(Message message) {
        messages.offer(message);
    }

    public Message await() throws InterruptedException {
        return messages.poll(1, TimeUnit.SECONDS);
    }
}
