package com.chatop.backend.dto;

import com.chatop.backend.entity.Message;
import com.chatop.backend.entity.Rental;
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
public class MessageDTO {
    private Long id;

    @NotNull(message = "Rental must not be null")
    private RentalDTO rental;

    @NotNull(message = "User must not be null")
    private UserDTO user;

    @NotNull(message = "Message content must not be null")
    @NotEmpty(message = "Message content must not be empty")
    @NotBlank(message = "Message content must not be blank")
    @Size(max = 2000, message = "Message content can be up to 2000 characters long")
    private String message;

    @NotNull(message = "Creation date must not be null")
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public static MessageDTO fromEntity(Message message){
        return MessageDTO.builder()
                    .id(message.getId())
                    .rental(RentalDTO.fromEntity(message.getRental()))
                    .user(UserDTO.fromEntity(message.getUser()))
                    .message(message.getMessage())
                    .createdAt(message.getCreatedAt())
                    .updatedAt(message.getUpdatedAt())
                .build();
    }

    public static Message toEntity(MessageDTO messageDTO){
        return Message.builder()
                    .id(messageDTO.getId())
                    .rental(RentalDTO.toEntity(messageDTO.getRental()))
                    .user(UserDTO.toEntity(messageDTO.getUser()))
                    .message(messageDTO.getMessage())
                    .createdAt(messageDTO.getCreatedAt())
                    .updatedAt(messageDTO.getUpdatedAt())
                .build();
    }
}
