package com.chatop.backend.dto;


import com.chatop.backend.entity.Rental;
import com.chatop.backend.entity.User;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RentalDTO {

    private Long id;

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

    // Permet de renvoyer l'objet owner (qui est un user) dans le json
    // @NotNull(message = "Owner must not be null")
    // private UserDTO owner;

    // Permet de renvoyer uniquement l'owner_id dans le json
    @NotNull(message = "Owner id must not be null")
    @Positive(message = "Owner id must be a positive value")
    private Long owner_id;


    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime created_at;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime updated_at;

    public static RentalDTO fromEntity(Rental rental) {
        return RentalDTO.builder()
                    .id(rental.getId())
                    .name(rental.getName())
                    .surface(rental.getSurface())
                    .price(rental.getPrice())
                    .picture(rental.getPicture())
                    .description(rental.getDescription())
                    //.owner(UserDTO.fromEntity(rental.getOwner()))
                    .owner_id(rental.getOwner().getId())
                    .created_at(rental.getCreated_at())
                    .updated_at(rental.getUpdated_at())
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
                    //.owner(UserDTO.toEntity(rentalDTO.getOwner()))
                    .owner(
                            User.builder()
                                    .id(rentalDTO.getOwner_id())
                                    .build()
                    )
                    .created_at(rentalDTO.getCreated_at())
                    .updated_at(rentalDTO.getUpdated_at())
                .build();
    }
}
