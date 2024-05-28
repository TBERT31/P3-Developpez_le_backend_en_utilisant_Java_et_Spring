package com.chatop.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthRequest {
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email is not compliant")
    private String email;

    @NotBlank(message = "Password must not be blank")
    private String password;
}
