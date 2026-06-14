package com.turkcell.bff_service.service;

import com.turkcell.bff_service.dto.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KeycloakAuthService {

    private final RestClient restClient;

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    public KeycloakAuthService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    /**
     * Kullanıcı adı ve şifre ile Keycloak'tan token alır (Resource Owner Password Grant).
     */
    public TokenResponse login(String username, String password) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "password");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("username", username);
        formData.add("password", password);

        return restClient.post()
                .uri(getTokenEndpoint())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .body(TokenResponse.class);
    }

    /**
     * Refresh token kullanarak yeni bir access token alır.
     */
    public TokenResponse refreshToken(String refreshToken) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "refresh_token");
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("refresh_token", refreshToken);

        return restClient.post()
                .uri(getTokenEndpoint())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .body(TokenResponse.class);
    }

    /**
     * Keycloak oturumunu sonlandırır.
     */
    public void logout(String refreshToken) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("refresh_token", refreshToken);

        restClient.post()
                .uri(getLogoutEndpoint())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .retrieve()
                .toBodilessEntity();
    }

    private String getTokenEndpoint() {
        return authServerUrl + "/realms/" + realm + "/protocol/openid-connect/token";
    }

    private String getLogoutEndpoint() {
        return authServerUrl + "/realms/" + realm + "/protocol/openid-connect/logout";
    }
}
