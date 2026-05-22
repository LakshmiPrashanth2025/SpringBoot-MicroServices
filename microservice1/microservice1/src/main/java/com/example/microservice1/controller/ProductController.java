package com.example.microservice1.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.microservice1.dto.Product;

@RestController
public class ProductController {
	
	List<Product> products = new ArrayList<>();

	@GetMapping("/products")
	public List<Product> getProducts() {
		return products;
	}

	@GetMapping("/products/{id}")
	public Product getProductById(@PathVariable int id) {

		if(new Random().nextBoolean()) {
			//throw new RuntimeException("Simulating random service failures");
		}
		
		for (Product product : products) {
			if (product.getId() == id) {
				return product;
			}
		}
		return null;
	}

	@PostMapping("/products") 
	public String addProduct(@RequestBody Product product) {
		products.add(product);
		return "Product added successfully";
	}
}
