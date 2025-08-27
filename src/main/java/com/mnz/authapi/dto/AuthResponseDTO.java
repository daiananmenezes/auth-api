package com.mnz.authapi.dto;

import java.util.List;

public record AuthResponseDTO(
        String token,
        String type,
        long expiresIn,
        String username,
        List<String> authorities
) {
    public static AuthResponseDTO of(String token, long expiresIn, String username, List<String> authorities) {
        return new AuthResponseDTO(token, "Bearer", expiresIn, username, authorities);
    }
}
