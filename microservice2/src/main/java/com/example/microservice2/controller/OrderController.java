package com.example.microservice2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.microservice2.dto.Product;
import com.example.microservice2.feign.OrderFeignClient;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
public class OrderController {
	
	private final WebClient webClient;
	
	private final OrderFeignClient feignClient;
	
	public OrderController(WebClient.Builder webClientBuilder
			, OrderFeignClient feignClient
	) {
		this.webClient = webClientBuilder.build();
		 this.feignClient = feignClient;
	}
	
	@GetMapping("/orders/product/{id}")
	@CircuitBreaker(name = "microservice1", fallbackMethod = "fallbackGetOrderDetailsById")
	public String getOrderById(@PathVariable int id) {

		Product p = webClient.get()  //https://localhost:8081/products/1
		        .uri("http://microservice1/products/" + id)
		        .retrieve()
		        .bodyToMono(Product.class)
		        .block();
		
		System.out.println("Product retrieved with Id: " + p.getId());
		//return "Ordered the product: " + p.getName() + " with price: " + p.getPrice();
		
		//If calling via feign client
		Product p1 = feignClient.getProductById(Long.valueOf(id));
		return "Ordered by feign client the product: " + p1.getName() + " with price: " + p1.getPrice();
	}
	

	@GetMapping("/orders/circuit/{id}")
	@CircuitBreaker(name = "microservice1", fallbackMethod = "fallbackGetOrderById")
	public Product getOrderByCircuitBreaker(@PathVariable Long id) {
		return feignClient.getProductById(id);
	}


	public Product fallbackGetOrderById(Long id, Exception t) {
		System.out.println("Fallback executed: " + t.getMessage());
		return new Product();
	}
	
	public String fallbackGetOrderDetailsById(int id, Exception t) {
		System.out.println("Fallback executed: " + t.getMessage());
		return "Circuit breaker fallback: Unable to retrieve product with id: " + id;
	}


}
