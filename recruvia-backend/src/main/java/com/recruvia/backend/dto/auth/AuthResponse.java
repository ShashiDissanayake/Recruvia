package com.recruvia.backend.dto.auth;

public record AuthResponse(

        String accessToken,

        String refreshToken,

        String tokenType,

        long expiresIn
) {
}