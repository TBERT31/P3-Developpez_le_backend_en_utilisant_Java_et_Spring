package com.chatop.backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrationRequest {
    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email is not compliant")
    private String email;

    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 6, max=64, message = "The password must be between 6 and 64 characters long")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,64}$",
            message = "The password must contain at least one letter, one number, and one special character"
    )
    private String password;
}
