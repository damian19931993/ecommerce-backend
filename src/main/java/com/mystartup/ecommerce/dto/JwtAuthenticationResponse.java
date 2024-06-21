package com.mystartup.ecommerce.dto;

public class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType = "Bearer";

    private Long userId; // Add userId field

    private String name;

    public JwtAuthenticationResponse(String accessToken, String name, Long userId) {
        this.accessToken = accessToken;
        this.name = name;
        this.userId = userId;
    }


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
