package com.mnz.authapi.controller;

import com.mnz.authapi.dto.UserDTO;
import com.mnz.authapi.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam String username, @RequestParam String password) {
        return ResponseEntity.ok(authService.register(username, password));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDTO request) {
        String token = authService.login(request.username(), request.password());
        return ResponseEntity.ok(token);
    }

}
