package com.chatop.backend.service.impl;

import com.chatop.backend.dto.RegistrationRequest;
import com.chatop.backend.dto.UserDTO;
import com.chatop.backend.entity.User;
import com.chatop.backend.repository.UserRepository;
import com.chatop.backend.service.UserService;
import com.chatop.backend.validator.RegistrationValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor // Indispensable pour éviter l'erreur : Variable 'userRepository' might not have been initialized
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<UserDTO> getUserById(Integer user_id) {

        Optional<User> user = userRepository.findById(user_id);

        return user.map(UserDTO::fromEntity);
    }

    @Override
    public void registerUser(RegistrationRequest registrationRequest) throws Exception {
        // Vérification si un utilisateur existe déjà avec cet email
        User existingUser = userRepository.findByEmail(registrationRequest.getEmail());

        if (existingUser != null) {
            throw new Exception("User already exists with email: " + registrationRequest.getEmail());
        }

        // Validation de l'email
        if (!RegistrationValidator.isValidEmail(registrationRequest.getEmail())) {
            throw new Exception("Invalid email format");
        }

        // Validation du nom
        if (!RegistrationValidator.isValidName(registrationRequest.getName())) {
            throw new Exception("Name must not be blank");
        }

        // Validation du mot de passe
        if (!RegistrationValidator.isValidPassword(registrationRequest.getPassword())) {
            throw new Exception("Invalid password format");
        }

        // Créer un nouvel utilisateur à partir des données de la demande
        User newUser = new User();
        newUser.setEmail(registrationRequest.getEmail());
        newUser.setName(registrationRequest.getName());
        newUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        newUser.setCreated_at(LocalDateTime.now());

        // Enregistrer le nouvel utilisateur dans la base de données
        userRepository.save(newUser);
    }
}
