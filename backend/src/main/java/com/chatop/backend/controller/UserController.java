package com.chatop.backend.controller;


import com.chatop.backend.dto.UserDTO;
import com.chatop.backend.entity.User;
import com.chatop.backend.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "Users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<Optional<UserDTO>> getUserById(@PathVariable Integer id){
        Optional<User> user = userService.getUserById(id.longValue());

        if (user.isPresent()) {
            UserDTO userDTO = UserDTO.fromEntity(user.get());
            return ResponseEntity.ok(Optional.of(userDTO));
        } else {
            return ResponseEntity.ok(Optional.empty());
        }
    }

}
