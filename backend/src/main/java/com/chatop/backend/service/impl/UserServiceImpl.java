package com.chatop.backend.service.impl;

import com.chatop.backend.dto.RegistrationRequest;
import com.chatop.backend.dto.UserDTO;
import com.chatop.backend.entity.User;
import com.chatop.backend.repository.UserRepository;
import com.chatop.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
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
        // Vérifier si un utilisateur existe déjà avec cet email
        User existingUser = userRepository.findByEmail(registrationRequest.getEmail());

        if (existingUser != null) {
            throw new Exception("User already exists with email: " + registrationRequest.getEmail());
        }

        // Créer un nouvel utilisateur à partir du DTO, car les vérification avec Jakarta sur les formats sont utiles
        UserDTO newUserDTO = UserDTO.builder()
                .email(registrationRequest.getEmail())
                .name(registrationRequest.getName())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .created_at(LocalDateTime.now())
                .build();

        User newUser = UserDTO.toEntity(newUserDTO);

        userRepository.save(newUser);
    }
}
