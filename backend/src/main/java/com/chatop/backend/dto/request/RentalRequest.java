package com.chatop.backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RentalRequest {

    @NotBlank(message = "Rental name must not be blank")
    @Size(min = 1, max = 64, message = "The name must be between 1 and 64 characters long")
    private String name;

    @NotNull(message = "Surface must not be null")
    @Positive(message = "Surface must be a positive value")
    private Double surface;

    @NotNull(message = "Price must not be null")
    @Positive(message = "Price must be a positive value")
    private Double price;

    @Size(max = 2000, message = "Description can be up to 2000 characters long")
    private String description;

    private MultipartFile picture;
}
