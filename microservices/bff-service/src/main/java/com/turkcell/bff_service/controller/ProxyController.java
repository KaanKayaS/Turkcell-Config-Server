package com.turkcell.bff_service.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

/**
 * BFF Proxy Controller — İstemciden gelen istekleri Gateway'e yönlendirir.
 * Session'daki access token'ı alıp Authorization header olarak ekler.
 * İstemci hiçbir zaman token ile uğraşmaz.
 */
@RestController
@RequestMapping("/api/proxy")
public class ProxyController {

    private final RestClient restClient;

    public ProxyController(RestClient.Builder restClientBuilder) {
        // Gateway URL'i — Eureka yerine direkt URL kullanıyoruz (basitlik için)
        this.restClient = restClientBuilder
                .baseUrl("http://localhost:9090")
                .build();
    }

    /**
     * GET isteklerini proxy'ler: /api/proxy/users/** → Gateway /api/users/**
     */
    @GetMapping("/{service}/**")
    public ResponseEntity<String> proxyGet(
            @PathVariable String service,
            HttpServletRequest request,
            HttpSession session) {

        String accessToken = (String) session.getAttribute("access_token");
        if (accessToken == null) {
            return ResponseEntity.status(401).body("{\"error\":\"Oturum bulunamadı. Lütfen giriş yapın.\"}");
        }

        // /api/proxy/users/1 → /api/users/1
        String targetPath = request.getRequestURI().replace("/api/proxy", "/api");

        String response = restClient.get()
                .uri(targetPath)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .body(String.class);

        return ResponseEntity.ok(response);
    }
}
