package com.lab5.order;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {
	private final Map<Long, Order> orders = new ConcurrentHashMap<>();
	private final AtomicLong idGenerator = new AtomicLong(2000);

	public OrderController() {
		Order sample = new Order(idGenerator.getAndIncrement(), 1000L, 2, 2400.00, "PLACED");
		orders.put(sample.id(), sample);
	}

	@GetMapping
	public List<Order> getOrders() {
		return new ArrayList<>(orders.values());
	}

	@PostMapping
	public ResponseEntity<Order> createOrder(@RequestBody CreateOrderRequest request) {
		long id = idGenerator.getAndIncrement();
		Order order = new Order(id, request.itemId(), request.quantity(), request.total(), "PLACED");
		orders.put(id, order);
		return ResponseEntity.status(HttpStatus.CREATED).body(order);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
		Order order = orders.get(id);
		if (order == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(order);
	}

	public record CreateOrderRequest(Long itemId, int quantity, double total) {
	}

	public record Order(Long id, Long itemId, int quantity, double total, String status) {
	}
}
