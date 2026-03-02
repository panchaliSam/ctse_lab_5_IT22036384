package com.lab5.item;

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
@RequestMapping("/items")
public class ItemController {
	private final Map<Long, Item> items = new ConcurrentHashMap<>();
	private final AtomicLong idGenerator = new AtomicLong(1000);

	public ItemController() {
		Item sample = new Item(idGenerator.getAndIncrement(), "Notebook", 1200.00, 50);
		items.put(sample.id(), sample);
	}

	@GetMapping
	public List<Item> getItems() {
		return new ArrayList<>(items.values());
	}

	@PostMapping
	public ResponseEntity<Item> createItem(@RequestBody CreateItemRequest request) {
		long id = idGenerator.getAndIncrement();
		Item item = new Item(id, request.name(), request.price(), request.quantity());
		items.put(id, item);
		return ResponseEntity.status(HttpStatus.CREATED).body(item);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		Item item = items.get(id);
		if (item == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(item);
	}

	public record CreateItemRequest(String name, double price, int quantity) {
	}

	public record Item(Long id, String name, double price, int quantity) {
	}
}
