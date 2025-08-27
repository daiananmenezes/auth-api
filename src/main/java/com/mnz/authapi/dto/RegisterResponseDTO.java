package com.mnz.authapi.dto;

import java.time.Instant;

public record RegisterResponseDTO(
        String message,
        String username,
        String createdAt
) {
    public static RegisterResponseDTO of(String username) {
        return new RegisterResponseDTO(
                "Usuário registrado com sucesso",
                username,
                Instant.now().toString()
        );
    }
}