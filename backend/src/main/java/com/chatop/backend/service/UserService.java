package com.chatop.backend.service;

import com.chatop.backend.dto.request.RegistrationRequest;
import com.chatop.backend.dto.UserDTO;
import com.chatop.backend.entity.User;
import org.springframework.validation.annotation.Validated;


import java.util.Optional;

@Validated
public interface UserService {
    Optional<User> getUserById(Long user_id);
    Optional<User> getUserByEmail(String email);
    User registerUser(RegistrationRequest registrationRequest) throws IllegalArgumentException;
}
