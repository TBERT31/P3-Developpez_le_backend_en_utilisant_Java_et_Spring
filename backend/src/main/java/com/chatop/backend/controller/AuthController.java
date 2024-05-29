package com.chatop.backend.controller;

import com.chatop.backend.dto.request.RegistrationRequest;
import com.chatop.backend.dto.response.AuthResponse;
import com.chatop.backend.dto.UserDTO;
import com.chatop.backend.entity.User;
import com.chatop.backend.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.chatop.backend.service.auth.MyUserDetailsService;
import com.chatop.backend.util.JwtUtil;
import com.chatop.backend.dto.request.AuthRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

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
    public ResponseEntity<AuthResponse> createAuthenticationToken(
            @Valid @RequestBody AuthRequest authRequest
    ) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authRequest.getEmail());

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(jwtUtil.generateToken(userDetails));

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(
            @Valid @RequestBody RegistrationRequest registrationRequest
    ) {
        userService.registerUser(registrationRequest);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(registrationRequest.getEmail(), registrationRequest.getPassword())
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(registrationRequest.getEmail());

        AuthResponse authResponse = new AuthResponse();
        authResponse.setToken(jwtUtil.generateToken(userDetails));

        return ResponseEntity.ok(authResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<Optional<UserDTO>> getMe(
            @RequestHeader("Authorization") String token
    ) {
        // Extraire le token de l'en-tête Authorization
        String jwt = token.substring(7);
        String email = jwtUtil.extractUsername(jwt);

        Optional<User> user = userService.getUserByEmail(email);

        if (user.isPresent()) {
            UserDTO userDTO = UserDTO.fromEntity(user.get());
            return ResponseEntity.ok(Optional.of(userDTO));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
