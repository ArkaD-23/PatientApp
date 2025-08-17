package com.pm.chat_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pm.chat_service.model.ChatMessageEntity;
import com.pm.chat_service.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ChatMessageService chatService;
    private final ObjectMapper mapper = new ObjectMapper();

    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // Parse JSON
        ChatMessageEntity msg = mapper.readValue(message.getPayload(), ChatMessageEntity.class);
        ChatMessageEntity saved = chatService.save(msg.getRoomId(), msg.getSender(), msg.getText(), msg.getType());

        String json = mapper.writeValueAsString(saved);

        for (WebSocketSession s : sessions) {
            s.sendMessage(new TextMessage(json));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
    }
}
