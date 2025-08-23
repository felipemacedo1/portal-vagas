package com.portalvagas.dto;

import com.portalvagas.domain.User;

public record AuthResponse(
    String accessToken,
    String refreshToken,
    UserDto user
) {
    public record UserDto(
        Long id,
        String email,
        User.Role role
    ) {}
}