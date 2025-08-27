package com.pm.gateway.controller;

import com.pm.gateway.dto.ChatMessage;
import com.pm.gateway.dto.ProfileDto;
import com.pm.gateway.ws.chat.ChatNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/v1/chat")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${user.service.url}")
    private String userServiceUrl;

    @Value("${chat.service.url}")
    private String chatServiceUrl;

    @GetMapping("/users")
    public ResponseEntity<List<ProfileDto>> findConnectedUsers() {
        ProfileDto[] profiles = restTemplate.getForObject(
                userServiceUrl + "/connected",
                ProfileDto[].class
        );

        return ResponseEntity.ok(List.of(profiles));
    }

    @MessageMapping("/chat.send")
    public void processMessage(@Payload ChatMessage chatMessage) {
        ChatMessage savedMsg = restTemplate.postForObject(
                chatServiceUrl + "/save",
                chatMessage,
                ChatMessage.class
        );

        ChatNotification notification = new ChatNotification(
                savedMsg.getId(),
                savedMsg.getSenderId(),
                savedMsg.getRecipientId(),
                savedMsg.getContent()
        );

        messagingTemplate.convertAndSend("/topic/messages/" + savedMsg.getRecipientId(), notification);

        messagingTemplate.convertAndSend("/topic/messages/" + savedMsg.getSenderId(), notification);
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(
            @PathVariable String senderId,
            @PathVariable String recipientId) {

        ChatMessage[] messages = restTemplate.getForObject(
                chatServiceUrl + "/messages/" + senderId + "/" + recipientId,
                ChatMessage[].class
        );

        List<ChatMessage> list = List.of(messages).stream()
                .peek(m -> m.setTimestamp(new Date(m.getTimestamp().getTime())))
                .collect(Collectors.toList());

        return ResponseEntity.ok(list);
    }
}
