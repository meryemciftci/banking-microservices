package com.banking.transfer.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "banking-exchange";
    public static final String TRANSACTION_QUEUE = "transaction-queue";
    public static final String NOTIFICATION_QUEUE = "notification-queue";
    public static final String TRANSACTION_ROUTING_KEY = "transaction.created";
    public static final String NOTIFICATION_ROUTING_KEY = "notification.transfer";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue transactionQueue() {
        return new Queue(TRANSACTION_QUEUE);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE);
    }

    @Bean
    public Binding transactionBinding(Queue transactionQueue, TopicExchange exchange) {
        return BindingBuilder.bind(transactionQueue).to(exchange).with(TRANSACTION_ROUTING_KEY);
    }

    @Bean
    public Binding notificationBinding(Queue notificationQueue, TopicExchange exchange) {
        return BindingBuilder.bind(notificationQueue).to(exchange).with(NOTIFICATION_ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}