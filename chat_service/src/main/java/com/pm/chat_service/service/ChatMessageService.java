package com.pm.chat_service.service;

import com.pm.chat_service.model.ChatMessageEntity;
import com.pm.chat_service.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository repo;

    public ChatMessageEntity save(String roomId, String from, String text, String type) {
        ChatMessageEntity msg = ChatMessageEntity.builder()
                .roomId(roomId)
                .sender(from)
                .text(text)
                .type(type)
                .ts(Instant.now())
                .build();
        return repo.save(msg);
    }
}
