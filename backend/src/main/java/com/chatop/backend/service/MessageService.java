package com.chatop.backend.service;

import com.chatop.backend.dto.MessageDTO;


import java.io.IOException;
import java.util.Optional;

public interface MessageService {
    Optional<MessageDTO> createMessage(MessageDTO messageDTO) throws IOException;
}
