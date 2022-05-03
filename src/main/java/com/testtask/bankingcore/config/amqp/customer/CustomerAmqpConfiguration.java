package com.testtask.bankingcore.config.amqp.customer;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CustomerAmqpProperties.class)
public class CustomerAmqpConfiguration {}
