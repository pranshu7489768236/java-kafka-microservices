package com.pranshu.inventoryservice.kafka;

import com.pranshu.inventoryservice.event.OrderEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class InventoryConsumer {

    @KafkaListener(
            topics = "order-created",
            groupId = "inventory-service-group"
    )
    public void consume(OrderEvent orderEvent) {

        System.out.println(
                "======================================"
        );

        System.out.println(
                "Inventory Service received order: "
                        + orderEvent.getId()
        );

        System.out.println(
                "Product: "
                        + orderEvent.getProductName()
        );

        System.out.println(
                "Quantity required: "
                        + orderEvent.getQuantity()
        );

        System.out.println(
                "Checking inventory..."
        );

        System.out.println(
                "Inventory available for order: "
                        + orderEvent.getId()
        );

        System.out.println(
                "======================================"
        );
    }
}