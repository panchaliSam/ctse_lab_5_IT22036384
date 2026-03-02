package com.lab5.payment;

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
@RequestMapping("/payments")
public class PaymentController {
	private final Map<Long, Payment> payments = new ConcurrentHashMap<>();
	private final AtomicLong idGenerator = new AtomicLong(3000);

	public PaymentController() {
		Payment sample = new Payment(idGenerator.getAndIncrement(), 2000L, 2400.00, "PROCESSED");
		payments.put(sample.id(), sample);
	}

	@GetMapping
	public List<Payment> getPayments() {
		return new ArrayList<>(payments.values());
	}

	@PostMapping("/process")
	public ResponseEntity<Payment> processPayment(@RequestBody CreatePaymentRequest request) {
		long id = idGenerator.getAndIncrement();
		Payment payment = new Payment(id, request.orderId(), request.amount(), "PROCESSED");
		payments.put(id, payment);
		return ResponseEntity.status(HttpStatus.CREATED).body(payment);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
		Payment payment = payments.get(id);
		if (payment == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(payment);
	}

	public record CreatePaymentRequest(Long orderId, double amount) {
	}

	public record Payment(Long id, Long orderId, double amount, String status) {
	}
}
