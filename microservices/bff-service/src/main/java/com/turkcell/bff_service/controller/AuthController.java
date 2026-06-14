package com.turkcell.bff_service.controller;

import com.turkcell.bff_service.dto.AuthResponse;
import com.turkcell.bff_service.dto.LoginRequest;
import com.turkcell.bff_service.dto.RefreshTokenRequest;
import com.turkcell.bff_service.dto.TokenResponse;
import com.turkcell.bff_service.service.KeycloakAuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final KeycloakAuthService keycloakAuthService;

    public AuthController(KeycloakAuthService keycloakAuthService) {
        this.keycloakAuthService = keycloakAuthService;
    }

    /**
     * Kullanıcı girişi — Token sunucuda session'da saklanır, istemciye JSESSIONID cookie gönderilir.
     * Access token ASLA istemciye döndürülmez.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        TokenResponse tokenResponse = keycloakAuthService.login(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );

        // Token'ları sunucu tarafında session'da sakla
        session.setAttribute("access_token", tokenResponse.getAccessToken());
        session.setAttribute("refresh_token", tokenResponse.getRefreshToken());
        session.setAttribute("token_type", tokenResponse.getTokenType());

        AuthResponse response = new AuthResponse(
                true,
                "Giriş başarılı. Token sunucuda saklandı.",
                session.getId()
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Token yenileme — refresh token session'dan alınır, yeni token session'a yazılır.
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(HttpSession session) {
        String refreshToken = (String) session.getAttribute("refresh_token");
        if (refreshToken == null) {
            return ResponseEntity.status(401).body(
                    new AuthResponse(false, "Session'da refresh token bulunamadı.", null)
            );
        }

        TokenResponse tokenResponse = keycloakAuthService.refreshToken(refreshToken);

        // Yeni token'ları session'a yaz
        session.setAttribute("access_token", tokenResponse.getAccessToken());
        session.setAttribute("refresh_token", tokenResponse.getRefreshToken());

        return ResponseEntity.ok(new AuthResponse(true, "Token yenilendi.", session.getId()));
    }

    /**
     * Çıkış — Keycloak oturumunu sonlandırır ve HTTP session'ı siler.
     */
    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(HttpSession session) {
        String refreshToken = (String) session.getAttribute("refresh_token");
        if (refreshToken != null) {
            keycloakAuthService.logout(refreshToken);
        }
        session.invalidate();

        return ResponseEntity.ok(new AuthResponse(false, "Çıkış yapıldı. Session silindi.", null));
    }

    /**
     * Oturum durumunu kontrol et — session'da token var mı?
     */
    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(HttpSession session) {
        String accessToken = (String) session.getAttribute("access_token");
        boolean isAuthenticated = accessToken != null;

        return ResponseEntity.ok(Map.of(
                "authenticated", isAuthenticated,
                "sessionId", session.getId(),
                "message", isAuthenticated
                        ? "Session aktif. Token sunucuda mevcut (istemciye gönderilmedi)."
                        : "Session'da token bulunamadı. Lütfen giriş yapın."
        ));
    }
}
