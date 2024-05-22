package com.chatop.backend.controller;

import com.chatop.backend.dto.RegistrationRequest;
import com.chatop.backend.dto.UserDTO;
import com.chatop.backend.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import com.chatop.backend.service.auth.MyUserDetailsService;
import com.chatop.backend.util.JwtUtil;
import com.chatop.backend.dto.AuthenticationRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> createAuthenticationToken(
            @RequestBody AuthenticationRequest authenticationRequest
    ) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
            );

            final UserDetails userDetails = userDetailsService
                    .loadUserByUsername(authenticationRequest.getEmail());

            final String jwt = jwtUtil.generateToken(userDetails);

            return ResponseEntity.ok(Map.of("token", jwt));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of("message", "Incorrect email or password")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("message", "An error occurred: " + e.getMessage())
            );
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(
            @RequestBody RegistrationRequest registrationRequest
    ) {
        try {
            userService.registerUser(registrationRequest);

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(registrationRequest.getEmail(), registrationRequest.getPassword())
            );

            final UserDetails userDetails = userDetailsService.loadUserByUsername(registrationRequest.getEmail());
            final String jwt = jwtUtil.generateToken(userDetails);

            return ResponseEntity.ok(
                    Map.of("token", jwt)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            Map.of("message", "Failed to register user: " + e.getMessage())
                    );
        }
    }

    @GetMapping("/me")
    public ResponseEntity<Optional<UserDTO>> getMe(@RequestHeader("Authorization") String token) {
        // Extraire le token de l'en-tête Authorization
        String jwt = token.substring(7);
        String email = jwtUtil.extractUsername(jwt);

        Optional<UserDTO> userDTO = userService.getUserByEmail(email);

        if (userDTO.isPresent()) {
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
