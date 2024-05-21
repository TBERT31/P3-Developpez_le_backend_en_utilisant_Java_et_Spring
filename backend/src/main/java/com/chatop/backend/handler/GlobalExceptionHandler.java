package com.chatop.backend.handler;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
    public ResponseEntity<ExceptionRepresentation> handleJwtException(Exception e) {
        ExceptionRepresentation representation = ExceptionRepresentation.builder()
                .message("Invalid or expired JWT token")
                .build();

        // Retourne une réponse uniforme en cas d'erreur JWT avec un message d'erreur spécifique.
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(representation);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionRepresentation> handleBadCredentialsException(){
        ExceptionRepresentation representation = ExceptionRepresentation.builder()
                .message("Your email and / or password is incorrect")
                .build();

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(representation);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionRepresentation> handleException(){
        ExceptionRepresentation representation = ExceptionRepresentation.builder()
                .message("This data already exist")
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(representation);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionRepresentation> handleDisabledException(){
        ExceptionRepresentation representation = ExceptionRepresentation.builder()
                .message("You cannot access your account because it is not activated")
                .build();

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(representation);
    }


}
