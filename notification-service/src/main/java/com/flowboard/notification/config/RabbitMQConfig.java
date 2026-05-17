package com.flowboard.notification.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@org.springframework.context.annotation.Profile("!test")
public class RabbitMQConfig {

    public static final String EXCHANGE = "flowboard.exchange";
    public static final String QUEUE = "notification.queue";
    public static final String ROUTING_KEY = "notification.key";

    @Bean
    public Queue notificationQueue() {
        return new Queue(QUEUE);
    }

    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding binding(Queue notificationQueue, TopicExchange notificationExchange) {
        return BindingBuilder.bind(notificationQueue).to(notificationExchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory) {
        org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory factory = 
                new org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(converter());
        return factory;
    }
}
