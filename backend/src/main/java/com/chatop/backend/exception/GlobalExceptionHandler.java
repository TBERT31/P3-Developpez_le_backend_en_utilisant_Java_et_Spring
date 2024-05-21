package com.chatop.backend.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
                    ExpiredJwtException.class,
                    UnsupportedJwtException.class,
                    MalformedJwtException.class,
                    SignatureException.class,
                    IllegalArgumentException.class
            })
    // Capture les exceptions JWT à un niveau global
    public ResponseEntity<?> handleJwtException(Exception e) {
        // Retourne une réponse uniforme en cas d'erreur JWT avec un message d'erreur spécifique.
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                Map.of("message", "Invalid or expired JWT token")
        );
    }
}
