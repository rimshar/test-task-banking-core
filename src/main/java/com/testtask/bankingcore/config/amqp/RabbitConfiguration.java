package com.testtask.bankingcore.config.amqp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RabbitConfiguration {

    @Bean
    Jackson2JsonMessageConverter jsonMessageConverter(ObjectMapper mapper) {
        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    @RabbitJson
    RabbitTemplate rabbitTemplate(
        Jackson2JsonMessageConverter jsonMessageConverter,
        RabbitTemplateConfigurer configurer,
        ConnectionFactory connectionFactory
    ) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        configurer.configure(rabbitTemplate, connectionFactory);
        return rabbitTemplate;
    }
}
