package com.testtask.bankingcore.config.amqp.transaction;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TransactionAmqpProperties.class)
public class TransactionAmqpConfiguration {}
