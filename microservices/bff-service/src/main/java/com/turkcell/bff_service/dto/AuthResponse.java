package com.turkcell.bff_service.dto;

/**
 * Login sonrası istemciye döndürülen yanıt.
 * Access token DÖNDÜRÜLMEZ — sunucu tarafında session'da saklanır.
 */
public class AuthResponse {
    private boolean authenticated;
    private String message;
    private String sessionId;

    public AuthResponse() {
    }

    public AuthResponse(boolean authenticated, String message, String sessionId) {
        this.authenticated = authenticated;
        this.message = message;
        this.sessionId = sessionId;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
