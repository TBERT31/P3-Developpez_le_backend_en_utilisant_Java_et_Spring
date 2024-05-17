package com.chatop.backend.controller;


import com.chatop.backend.dto.AuthenticationRequest;
import com.chatop.backend.dto.AuthenticationResponse;
import com.chatop.backend.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import com.chatop.backend.service.auth.JWTService;

import org.springframework.security.core.Authentication;


@RestController
@RequestMapping("/auth")
@CrossOrigin("http://localhost:4200")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {

    public final JWTService jwtService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> getToken(
            Authentication authentication
    ) {
//        Authentication authentication = new UsernamePasswordAuthenticationToken(
//                authenticationRequest.getEmail(),
//                authenticationRequest.getPassword()
//        );
        String token = jwtService.generateToken(authentication);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(token);
        return ResponseEntity.ok(authenticationResponse);
    }

}
