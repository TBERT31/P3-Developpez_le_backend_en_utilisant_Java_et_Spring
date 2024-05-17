package com.chatop.backend.service;

import com.chatop.backend.dto.AuthenticationRequest;
import com.chatop.backend.dto.AuthenticationResponse;
import com.chatop.backend.dto.UserDTO;

import java.util.Optional;

public interface UserService {

    Optional<UserDTO> getUserById(Integer user_id);
    AuthenticationResponse register(UserDTO userDTO);
    AuthenticationResponse login(AuthenticationRequest request);
}
