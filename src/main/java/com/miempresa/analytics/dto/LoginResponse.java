package com.miempresa.analytics.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse {
    
    @JsonProperty("token")
    private String token;
    
    @JsonProperty("type")
    private String type = "Bearer";
    
    @JsonProperty("username")
    private String username;
    
    @JsonProperty("expiresIn")
    private Long expiresIn; // en segundos

    // Constructores
    public LoginResponse() {
    }

    public LoginResponse(String token, String username, Long expiresIn) {
        this.token = token;
        this.username = username;
        this.expiresIn = expiresIn;
    }

    // Getters y Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }
}


