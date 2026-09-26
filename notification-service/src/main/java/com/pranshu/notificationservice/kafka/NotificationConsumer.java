package com.pranshu.notificationservice.kafka;

import com.pranshu.notificationservice.event.OrderEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {

    @KafkaListener(
            topics = "order-created",
            groupId = "notification-service-group"
    )
    public void consume(OrderEvent orderEvent) {

        System.out.println(
                "======================================"
        );

        System.out.println(
                "Notification Service received order: "
                        + orderEvent.getId()
        );

        System.out.println(
                "Sending order confirmation..."
        );

        System.out.println(
                "Notification sent to customer: "
                        + orderEvent.getCustomerName()
        );

        System.out.println(
                "Order confirmation sent for order: "
                        + orderEvent.getId()
        );

        System.out.println(
                "======================================"
        );
    }
}