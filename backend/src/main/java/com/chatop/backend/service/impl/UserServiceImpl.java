package com.chatop.backend.service.impl;

import com.chatop.backend.dto.UserDTO;
import com.chatop.backend.entity.User;
import com.chatop.backend.repository.UserRepository;
import com.chatop.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor // Indispensable pour éviter l'erreur : Variable 'userRepository' might not have been initialized
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Optional<UserDTO> getUserById(Integer user_id) {

        Optional<User> user = userRepository.findById(user_id);

        return user.map(UserDTO::fromEntity);
    }

//    @Override
//    @Transactional
//    public AuthenticationResponse register(UserDTO userDTO) {
//        User user = UserDTO.toEntity(userDTO);
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//
//        var savedUser = userRepository.save(user);
//
//
//    }

}
