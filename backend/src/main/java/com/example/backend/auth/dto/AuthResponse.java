package com.example.backend.auth.dto;

public class AuthResponse {
    private String accessToken;
    private Long userId;
    private String name;
    private String email;
    private String role;

    public AuthResponse(String accessToken, Long userId, String name, String email, String role) {
        this.accessToken = accessToken;
        this.userId    = userId;
        this.name      = name;
        this.email     = email;
        this.role      = role;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
