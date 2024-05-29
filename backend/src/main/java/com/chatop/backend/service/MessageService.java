package com.chatop.backend.service;

import com.chatop.backend.entity.Message;

import java.util.Optional;

public interface MessageService {

    Optional<Message> createMessage(Message message);

}
