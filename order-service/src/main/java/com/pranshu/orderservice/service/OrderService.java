package com.pranshu.orderservice.service;

import com.pranshu.orderservice.entity.Order;
import com.pranshu.orderservice.kafka.OrderEventProducer;
import com.pranshu.orderservice.repository.OrderRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final RedisTemplate<String, Object> redisTemplate;
	private final OrderEventProducer orderEventProducer;

	public OrderService(OrderRepository orderRepository, RedisTemplate<String, Object> redisTemplate,
			OrderEventProducer orderEventProducer) {

		this.orderRepository = orderRepository;
		this.redisTemplate = redisTemplate;
		this.orderEventProducer = orderEventProducer;
	}

	public Order createOrder(Order order, String idempotencyKey) {

		String key = "idempotency:" + idempotencyKey;

		Object existingOrder = redisTemplate.opsForValue().get(key);

		if (existingOrder != null) {
			return (Order) existingOrder;
		}

		order.setStatus("CREATED");
		order.setCreatedAt(LocalDateTime.now());

		Order savedOrder = orderRepository.save(order);
		orderEventProducer.sendOrderCreatedEvent(savedOrder);
		redisTemplate.opsForValue().set(key, savedOrder, 10, TimeUnit.MINUTES);

		String orderCacheKey = "order:" + savedOrder.getId();

		redisTemplate.opsForValue().set(orderCacheKey, savedOrder, 10, TimeUnit.MINUTES);

		return savedOrder;
	}

	public List<Order> getAllOrders() {
		return orderRepository.findAll();
	}

	public Optional<Order> getOrderById(Long id) {

		String key = "order:" + id;

		Object cachedOrder = redisTemplate.opsForValue().get(key);

		if (cachedOrder != null) {
			return Optional.of((Order) cachedOrder);
		}

		Optional<Order> order = orderRepository.findById(id);

		if (order.isPresent()) {
			redisTemplate.opsForValue().set(key, order.get(), 10, TimeUnit.MINUTES);
		}

		return order;
	}
}