package com.chatop.backend.dto;


import com.chatop.backend.entity.Rental;
import com.chatop.backend.entity.User;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RentalDTO {

    private Long id;

    @NotNull(message = "Rental name must not be null")
    @NotEmpty(message = "Rental name must not be empty")
    @NotBlank(message = "Rental name must not be blank")
    @Size(min = 1, max = 64, message = "The name must be between 1 and 64 characters long")
    private String name;

    @NotNull(message = "Surface must not be null")
    @Positive(message = "Surface must be a positive value")
    private Double surface;

    @NotNull(message = "Price must not be null")
    @Positive(message = "Price must be a positive value")
    private Double price;

    private String picture;

    @Size(max = 2000, message = "Description can be up to 2000 characters long")
    private String description;

    @NotNull(message = "Owner must not be null")
    private UserDTO owner;

    @NotNull(message = "Creation date must not be null")
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static RentalDTO fromEntity(Rental rental) {
        return RentalDTO.builder()
                    .id(rental.getId())
                    .name(rental.getName())
                    .surface(rental.getSurface())
                    .price(rental.getPrice())
                    .picture(rental.getPicture())
                    .description(rental.getDescription())
                    .owner(UserDTO.fromEntity(rental.getOwner()))
                    .createdAt(rental.getCreatedAt())
                    .updatedAt(rental.getUpdatedAt())
                .build();
    }

    public static Rental toEntity(RentalDTO rentalDTO) {
        return Rental.builder()
                    .id(rentalDTO.getId())
                    .name(rentalDTO.getName())
                    .surface(rentalDTO.getSurface())
                    .price(rentalDTO.getPrice())
                    .picture(rentalDTO.getPicture())
                    .description(rentalDTO.getDescription())
                    .owner(UserDTO.toEntity(rentalDTO.getOwner()))
                    .createdAt(rentalDTO.getCreatedAt())
                    .updatedAt(rentalDTO.getUpdatedAt())
                .build();
    }
}
