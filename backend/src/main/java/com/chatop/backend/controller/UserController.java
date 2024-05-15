package com.chatop.backend.controller;


import com.chatop.backend.dto.UserDTO;
import com.chatop.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
@CrossOrigin("http://localhost:4200")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<Optional<UserDTO>> getUserById(@PathVariable Integer id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

}
