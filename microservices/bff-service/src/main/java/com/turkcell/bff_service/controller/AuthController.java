package com.turkcell.bff_service.controller;

import com.turkcell.bff_service.dto.LoginRequest;
import com.turkcell.bff_service.dto.RefreshTokenRequest;
import com.turkcell.bff_service.dto.TokenResponse;
import com.turkcell.bff_service.service.KeycloakAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final KeycloakAuthService keycloakAuthService;

    public AuthController(KeycloakAuthService keycloakAuthService) {
        this.keycloakAuthService = keycloakAuthService;
    }

    /**
     * Kullanıcı girişi — username/password ile Keycloak'tan token alır.
     */
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
        TokenResponse tokenResponse = keycloakAuthService.login(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );
        return ResponseEntity.ok(tokenResponse);
    }

    /**
     * Token yenileme — refresh token ile yeni access token alır.
     */
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        TokenResponse tokenResponse = keycloakAuthService.refreshToken(
                refreshTokenRequest.getRefreshToken()
        );
        return ResponseEntity.ok(tokenResponse);
    }

    /**
     * Çıkış — Keycloak oturumunu sonlandırır.
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        keycloakAuthService.logout(refreshTokenRequest.getRefreshToken());
        return ResponseEntity.noContent().build();
    }
}
