package com.mnz.authapi.service;

import com.mnz.authapi.entity.User;
import com.mnz.authapi.repository.UserRepositoryJPA;
import com.mnz.authapi.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {

    private final UserRepositoryJPA userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepositoryJPA userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.jwtUtil = new JwtUtil();
    }

    public String register(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("Usuário já existe");
        }
        String encodedPassword = passwordEncoder.encode(password);
        userRepository.save(new User(null, username, encodedPassword, "ROLE_USER"));
        return "Usuário registrado com sucesso!";
    }

    public String login(String username, String password) {
        log.info("Entrou login");
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Senha inválida");
        }

        return jwtUtil.generateToken(username);
    }
}
