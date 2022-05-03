package com.testtask.bankingcore.common.extensions;

import org.springframework.amqp.core.Message;

public class RabbitMessageExtension {

    public static String json(Message message) {
        return new String(message.getBody());
    }
}
