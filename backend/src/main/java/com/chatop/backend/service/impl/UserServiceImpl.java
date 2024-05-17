package com.chatop.backend.service.impl;

import com.chatop.backend.config.JwtUtils;
import com.chatop.backend.dto.AuthenticationRequest;
import com.chatop.backend.dto.AuthenticationResponse;
import com.chatop.backend.dto.UserDTO;
import com.chatop.backend.entity.User;
import com.chatop.backend.repository.UserRepository;
import com.chatop.backend.service.UserService;
import com.chatop.backend.validator.ObjectsValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor // Indispensable pour éviter l'erreur : Variable 'userRepository' might not have been initialized
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ObjectsValidator<UserDTO> validator;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authManager;

    @Override
    public Optional<UserDTO> getUserById(Integer user_id) {

        Optional<User> user = userRepository.findById(user_id);

        return user.map(UserDTO::fromEntity);
    }

    @Override
    @Transactional
    public AuthenticationResponse register(UserDTO dto) {
        validator.validate(dto);
        User user = UserDTO.toEntity(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        var savedUser = userRepository.save(user);
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", savedUser.getId());
        claims.put("name", savedUser.getName());
        String token = jwtUtils.generateToken((UserDetails) savedUser, claims);
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        final User user = userRepository.findByEmail(request.getEmail()).get();
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", user.getId());
        claims.put("name", user.getName());
        final String token = jwtUtils.generateToken((UserDetails) user, claims);
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

}
