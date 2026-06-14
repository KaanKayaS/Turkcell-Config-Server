package com.turkcell.product_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getProducts() {
        return ResponseEntity.ok(List.of(
                Map.of("id", 101, "name", "Laptop", "price", 25000.0),
                Map.of("id", 102, "name", "Telefon", "price", 15000.0)
        ));
    }
}
