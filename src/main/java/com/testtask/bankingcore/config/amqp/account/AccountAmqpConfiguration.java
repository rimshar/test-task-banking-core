package com.testtask.bankingcore.config.amqp.account;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AccountAmqpProperties.class)
public class AccountAmqpConfiguration {}
