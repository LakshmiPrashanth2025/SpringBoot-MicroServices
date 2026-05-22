package com.example.microservice2.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.microservice2.dto.Product;

@FeignClient(name = "microservice1")
public interface OrderFeignClient {
	
	// Feign client is a declarative way of calling another micro-service and the URL/ API call of that microservice

	@GetMapping("/products/{id}")
	Product getProductById(@PathVariable Long id);
}