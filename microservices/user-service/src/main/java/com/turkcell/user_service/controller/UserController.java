package com.turkcell.user_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping
    public ResponseEntity<List<Map<String, String>>> getUsers() {
        return ResponseEntity.ok(List.of(
                Map.of("id", "1", "name", "Ali Yılmaz", "role", "USER"),
                Map.of("id", "2", "name", "Ayşe Demir", "role", "ADMIN")
        ));
    }
}
