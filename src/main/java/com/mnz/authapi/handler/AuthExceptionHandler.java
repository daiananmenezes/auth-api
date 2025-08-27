package com.mnz.authapi.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException e, WebRequest request) {
        if (isSystemException(e) || isFrameworkPath(request)) {
            throw e;
        }

        Map<String, Object> error = new HashMap<>();
        error.put("error", e.getMessage());
        error.put("timestamp", Instant.now());
        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put("path", request.getDescription(false));

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException e, WebRequest request) {
        String errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        Map<String, Object> error = new HashMap<>();
        error.put("error", "Dados inválidos: " + errors);
        error.put("timestamp", Instant.now());
        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put("path", request.getDescription(false));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler({
            org.springframework.security.core.AuthenticationException.class,
            org.springframework.security.access.AccessDeniedException.class
    })
    public ResponseEntity<Map<String, Object>> handleSecurityException(Exception e, WebRequest request) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", "Erro de autenticação/autorização");
        error.put("timestamp", Instant.now());
        error.put("status", HttpStatus.UNAUTHORIZED.value());
        error.put("path", request.getDescription(false));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }


    private boolean isSystemException(RuntimeException e) {
        String className = e.getClass().getSimpleName();
        return className.contains("Bean") ||
                className.contains("Context") ||
                className.contains("Handler") ||
                className.contains("Servlet") ||
                className.contains("Mvc");
    }

    private boolean isFrameworkPath(WebRequest request) {
        String path = request.getDescription(false);
        return path.contains("/v3/api-docs") ||
                path.contains("/swagger-ui") ||
                path.contains("/actuator") ||
                path.contains("/h2-console");
    }
}