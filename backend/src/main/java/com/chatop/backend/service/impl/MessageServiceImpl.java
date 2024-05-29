package com.chatop.backend.service.impl;

import com.chatop.backend.dto.MessageDTO;
import com.chatop.backend.entity.Message;
import com.chatop.backend.repository.MessageRepository;
import com.chatop.backend.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;

    @Override
    public Optional<Message> createMessage(Message message) {
        return Optional.of(messageRepository.save(message));
    }
}
