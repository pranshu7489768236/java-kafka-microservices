package com.pranshu.orderservice.kafka;

import com.pranshu.orderservice.entity.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderEventProducer {

    private static final String TOPIC = "order-created";

    private final KafkaTemplate<String, Order> kafkaTemplate;

    public OrderEventProducer(KafkaTemplate<String, Order> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderCreatedEvent(Order order) {

        kafkaTemplate.send(
                TOPIC,
                String.valueOf(order.getId()),
                order
        );
    }
}