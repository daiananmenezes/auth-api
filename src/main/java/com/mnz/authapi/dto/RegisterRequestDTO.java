package com.mnz.authapi.dto;

public record RegisterRequestDTO(
        String username,
        String password,
        String email
) {}
