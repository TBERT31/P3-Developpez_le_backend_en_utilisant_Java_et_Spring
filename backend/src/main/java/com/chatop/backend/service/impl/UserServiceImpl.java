package com.chatop.backend.service.impl;

import com.chatop.backend.dto.request.RegistrationRequest;
import com.chatop.backend.entity.User;
import com.chatop.backend.repository.UserRepository;
import com.chatop.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> getUserById(Long user_id) {
        return userRepository.findById(user_id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    @Override
    public User registerUser(RegistrationRequest registrationRequest) throws IllegalArgumentException {
        // Vérification si un utilisateur existe déjà avec cet email
        User existingUser = userRepository.findByEmail(registrationRequest.getEmail());

        if (existingUser != null) {
            throw new IllegalArgumentException("User already exists with email: " + registrationRequest.getEmail());
        }

        // Créer un nouvel utilisateur à partir des données de la demande
        User newUser = new User();
        newUser.setEmail(registrationRequest.getEmail());
        newUser.setName(registrationRequest.getName());
        newUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        newUser.setCreated_at(LocalDateTime.now());

        // Enregistrer le nouvel utilisateur dans la base de données
        return userRepository.save(newUser);
    }
}
