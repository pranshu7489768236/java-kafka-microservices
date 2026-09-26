package com.pranshu.paymentservice.kafka;

import com.pranshu.paymentservice.event.OrderEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentConsumer {

    @KafkaListener(
            topics = "order-created",
            groupId = "payment-service-group"
    )
    public void consume(OrderEvent orderEvent) {

        System.out.println(
                "======================================"
        );

        System.out.println(
                "Payment Service received order: "
                        + orderEvent.getId()
        );

        System.out.println(
                "Customer: "
                        + orderEvent.getCustomerName()
        );

        System.out.println(
                "Product: "
                        + orderEvent.getProductName()
        );

        System.out.println(
                "Amount: "
                        + orderEvent.getPrice()
        );

        System.out.println(
                "Payment processing..."
        );

        System.out.println(
                "Payment successful for order: "
                        + orderEvent.getId()
        );

        System.out.println(
                "======================================"
        );
    }
}