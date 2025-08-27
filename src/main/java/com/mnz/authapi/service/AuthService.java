package com.mnz.authapi.service;

import com.mnz.authapi.dto.AuthResponseDTO;
import com.mnz.authapi.dto.RegisterResponseDTO;
import com.mnz.authapi.entity.User;
import com.mnz.authapi.repository.UserRepositoryJPA;
import com.mnz.authapi.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AuthService {

    private final UserRepositoryJPA userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepositoryJPA userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public RegisterResponseDTO register(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Usuário já existe");
        }

        String encodedPassword = passwordEncoder.encode(password);
        userRepository.save(new User(null, username, encodedPassword, "ROLE_USER"));

        return RegisterResponseDTO.of(username);
    }

    public AuthResponseDTO login(String username, String password) {
        log.info("Tentativa de login para usuário: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Senha inválida");
        }

        String token = jwtUtil.generateToken(username);
        long expiresIn = jwtUtil.getExpirationTime();
        List<String> authorities = List.of(user.getRole());

        log.info("Login realizado com sucesso para usuário: {}", username);

        return AuthResponseDTO.of(token, expiresIn, username, authorities);
    }

    public boolean validateToken(String token) {
        try {
            String username = jwtUtil.extractUsername(token);
            return jwtUtil.isTokenValid(token, username);
        } catch (Exception e) {
            log.error("Erro ao validar token: {}", e.getMessage());
            return false;
        }
    }

    public AuthResponseDTO refreshToken(String oldToken) {
        try {
            String username = jwtUtil.extractUsername(oldToken);

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            String newToken = jwtUtil.generateToken(username);
            long expiresIn = jwtUtil.getExpirationTime();
            List<String> authorities = List.of(user.getRole());

            return AuthResponseDTO.of(newToken, expiresIn, username, authorities);
        } catch (Exception e) {
            throw new RuntimeException("Token inválido para renovação");
        }
    }
}