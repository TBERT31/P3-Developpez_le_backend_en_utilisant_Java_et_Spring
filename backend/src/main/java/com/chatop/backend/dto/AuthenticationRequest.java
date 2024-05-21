package com.chatop.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthenticationRequest {
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email is not compliant")
    private String email;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, max=64, message = "The password must be between 6 and 64 characters long")
    private String password;
}
