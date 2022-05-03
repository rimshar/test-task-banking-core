package com.testtask.bankingcore.config.amqp.balance;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(BalanceAmqpProperties.class)
public class BalanceAmqpConfiguration {}
