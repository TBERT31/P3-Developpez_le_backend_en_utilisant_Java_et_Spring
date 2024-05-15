package com.chatop.backend.dto;


import com.chatop.backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserDTO {

    private Long id;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email is not compliant")
    private String email;

    @NotBlank(message = "User name must not be blank")
    private String name;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, max=64, message = "The password must be between 6 and 64 characters long")
    private String password;

    @NotNull(message = "Creation date must not be null")
    private LocalDateTime created_at;

    private LocalDateTime updated_at;

    public static UserDTO fromEntity(User user) {
        return UserDTO.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .password(user.getPassword())
                    .created_at(user.getCreated_at())
                    .updated_at(user.getUpdated_at())
                .build();
    }

    public static User toEntity(UserDTO userDTO) {
        return User.builder()
                    .id(userDTO.getId())
                    .email(userDTO.getEmail())
                    .name(userDTO.getName())
                    .password(userDTO.getPassword())
                    .created_at(userDTO.getCreated_at())
                    .updated_at(userDTO.getUpdated_at())
                .build();
    }
}
