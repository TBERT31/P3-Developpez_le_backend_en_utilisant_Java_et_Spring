package com.chatop.backend.controller;

import com.chatop.backend.dto.RegistrationRequest;
import com.chatop.backend.dto.UserDTO;
import com.chatop.backend.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import com.chatop.backend.service.auth.MyUserDetailsService;
import com.chatop.backend.util.JwtUtil;
import com.chatop.backend.dto.AuthenticationRequest;
import com.chatop.backend.dto.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(
            @RequestBody AuthenticationRequest authenticationRequest
    ) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
            );

            final UserDetails userDetails = userDetailsService
                    .loadUserByUsername(authenticationRequest.getEmail());

            final String jwt = jwtUtil.generateToken(userDetails);

            return ResponseEntity.ok(new AuthenticationResponse(jwt));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new AuthenticationResponse("Incorrect email or password")
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new AuthenticationResponse("An error occurred: " + e.getMessage())
            );
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(
            @RequestBody RegistrationRequest registrationRequest
    ) {
        try {
            userService.registerUser(registrationRequest);

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(registrationRequest.getEmail(), registrationRequest.getPassword())
            );

            final UserDetails userDetails = userDetailsService.loadUserByUsername(registrationRequest.getEmail());
            final String jwt = jwtUtil.generateToken(userDetails);

            return ResponseEntity.ok(new AuthenticationResponse(jwt));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthenticationResponse("Failed to register user: " + e.getMessage()));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMe(@RequestHeader("Authorization") String token) {
        // Extraire le token de l'en-tête Authorization
        String jwt = token.substring(7);
        String email = jwtUtil.extractEmail(jwt);

        Optional<UserDTO> userDTO = userService.getUserByEmail(email);

        if (userDTO.isPresent()) {
            return ResponseEntity.ok(userDTO.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
