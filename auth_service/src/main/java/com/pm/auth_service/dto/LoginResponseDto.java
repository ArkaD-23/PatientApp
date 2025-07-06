package com.pm.auth_service.dto;

public class LoginResponseDto {
    private boolean status;
    private String token;

    public LoginResponseDto(boolean status, String token) {
        this.status = status;
        this.token = token;
    }

    public boolean isStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }
}