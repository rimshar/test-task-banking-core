package com.testtask.bankingcore.config.amqp;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Declarable;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.testtask.bankingcore.config.amqp.AmqpProperties.AmqpExchange;
import com.testtask.bankingcore.config.amqp.AmqpProperties.AmqpQueue;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Configuration
@EnableConfigurationProperties(AmqpProperties.class)
class AmqpConfiguration {

    private final AmqpProperties props;

    @Bean
    Declarables topology() {
        return toDeclarables(props);
    }

    private Declarables toDeclarables(AmqpProperties props) {
        return new Declarables(
            Stream.of(
                toExchanges(props.exchanges()).stream().map(Declarable.class::cast).toList(),
                toQueues(props.queues()).stream().map(Declarable.class::cast).toList(),
                toBindings(props).stream().map(Declarable.class::cast).toList()
            ).flatMap(Collection::stream).toList()
        );
    }

    private List<Binding> toBindings(AmqpProperties props) {
        return props.queues().stream().map(this::bind).toList();
    }

    private Binding bind(AmqpProperties.AmqpQueue queue) {
        return new Binding(
            queue.name(),
            Binding.DestinationType.QUEUE,
            queue.boundTo().exchange(),
            queue.boundTo().routingKey(),
            Map.of()
        );
    }

    private List<Exchange> toExchanges(List<AmqpExchange> exchanges) {
        return exchanges.stream().map(
            exchange -> (Exchange) ExchangeBuilder.topicExchange(exchange.name()).build()
        ).toList();
    }

    private List<Queue> toQueues(List<AmqpQueue> queues) {
        return queues.stream().map(
            queue -> QueueBuilder.durable(queue.name())
                .withArgument("exchange", queue.boundTo().exchange())
                .withArgument("routingKey", queue.boundTo().routingKey())
                .build()
        ).toList();
    }
}
