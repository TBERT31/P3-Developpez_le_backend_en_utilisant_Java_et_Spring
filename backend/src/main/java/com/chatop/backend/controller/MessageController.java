package com.chatop.backend.controller;

import com.chatop.backend.dto.MessageDTO;
import com.chatop.backend.service.MessageService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/messages")
@CrossOrigin("http://localhost:4200")
@RequiredArgsConstructor
@Tag(name = "Messages")
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/")
    public ResponseEntity<Map<String, String>> createMessage(
            @RequestBody MessageDTO messageDTO
    ) {

        try {
            Optional<MessageDTO> createdMessage = messageService.createMessage(messageDTO);
            if (createdMessage.isPresent()) {
                return ResponseEntity.ok(
                        Map.of("message", "Message sent successfully")
                );
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                        Map.of("message", "Failed to send message")
                );
            }
        } catch (IOException e) {
            System.err.println("Failed to send message");
            e.printStackTrace(System.err);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("message", "Failed to send message")
            );
        }

    }

}
