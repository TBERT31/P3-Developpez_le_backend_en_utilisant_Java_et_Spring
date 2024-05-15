package com.chatop.backend.dto;

import com.chatop.backend.entity.Message;
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
public class MessageDTO {
    private Long id;

    // Permet de renvoyer l'objet rental dans le json
    // @NotNull(message = "Rental must not be null")
    // private RentalDTO rental;

    // Permet de renvoyer uniquement le rental_id dans le json
    @NotNull(message = "Rental id must not be null")
    @Positive(message = "Rental id must be a positive value")
    private Long rental_id;

    // Permet de renvoyer l'objet user dans le json
    // @NotNull(message = "User must not be null")
    // private UserDTO user;

    // Permet de renvoyer uniquement l'user_id dans le json
    @NotNull(message = "User id must not be null")
    @Positive(message = "User id must be a positive value")
    private Long user_id;

    @NotBlank(message = "Message content must not be blank")
    @Size(max = 2000, message = "Message content can be up to 2000 characters long")
    private String message;

    @NotNull(message = "Creation date must not be null")
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime created_at;

    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDateTime updated_at;

    public static MessageDTO fromEntity(Message message){
        return MessageDTO.builder()
                    .id(message.getId())
                    //.rental(RentalDTO.fromEntity(message.getRental()))
                    //.user(UserDTO.fromEntity(message.getUser()))
                    .rental_id(message.getRental().getId())
                    .user_id(message.getUser().getId())
                    .message(message.getMessage())
                    .created_at(message.getCreated_at())
                    .updated_at(message.getUpdated_at())
                .build();
    }

    public static Message toEntity(MessageDTO messageDTO){
        return Message.builder()
                    .id(messageDTO.getId())
                    //.rental(RentalDTO.toEntity(messageDTO.getRental()))
                    //.user(UserDTO.toEntity(messageDTO.getUser()))
                    .rental(
                        Rental.builder()
                                .id(messageDTO.getRental_id())
                                .build()
                    )
                    .user(
                        User.builder()
                                .id(messageDTO.getUser_id())
                                .build()
                    )
                    .message(messageDTO.getMessage())
                    .created_at(messageDTO.getCreated_at())
                    .updated_at(messageDTO.getUpdated_at())
                .build();
    }
}
