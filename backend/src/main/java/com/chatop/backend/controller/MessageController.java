package com.chatop.backend.controller;

import com.chatop.backend.dto.MessageDTO;
import com.chatop.backend.dto.response.MessageResponse;
import com.chatop.backend.service.MessageService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



import java.util.Optional;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Tag(name = "Messages")
public class MessageController {

    private final MessageService messageService;

    @PostMapping("")
    public ResponseEntity<MessageResponse> createMessage(
           @Valid @RequestBody MessageDTO messageDTO
    ) {
        MessageResponse messageResponse = new MessageResponse();

        Optional<MessageDTO> createdMessage = messageService.createMessage(messageDTO);
        if (createdMessage.isPresent()) {
            messageResponse.setMessage("Message sent successfully");
            return ResponseEntity.ok(messageResponse);
        } else {
            messageResponse.setMessage("Failed to send message");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageResponse);
        }
    }

}
