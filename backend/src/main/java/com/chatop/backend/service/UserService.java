package com.chatop.backend.service;

import com.chatop.backend.dto.UserDTO;

import java.util.Optional;

public interface UserService {

    Optional<UserDTO> getUserById(Integer user_id);

}
