package com.pm.chat_service.controller;

import com.pm.chat_service.chat.ChatMessageService;
import com.pm.chat_service.chatroom.ChatRoomService;
import com.pm.chat_service.chat.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ChatRestController {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    @PostMapping("/message")
    public ResponseEntity<ChatMessage> saveMessage(@RequestBody ChatMessage request) {

        ChatMessage saved = chatMessageService.save(request);

        ChatMessage response = new ChatMessage(
                saved.getId().toString(),
                saved.getRoomId(),
                saved.getSenderId(),
                saved.getRecipientId(),
                saved.getContent(),
                saved.getTimestamp()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> getMessages(
            @PathVariable String senderId,
            @PathVariable String recipientId) {

        var messages = chatMessageService.findChatMessages(senderId, recipientId);

        List<ChatMessage> response = messages.stream()
                .map(m -> new ChatMessage(
                        m.getId(),
                        m.getRoomId(),
                        m.getSenderId(),
                        m.getRecipientId(),
                        m.getContent(),
                        m.getTimestamp()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/room/{senderId}/{recipientId}")
    public ResponseEntity<String> getChatRoomId(
            @PathVariable String senderId,
            @PathVariable String recipientId) {

        boolean createIfNotExists = true;

        var chatIdOpt = chatRoomService.getChatRoomId(senderId, recipientId, createIfNotExists);

        return ResponseEntity.ok(chatIdOpt.orElse(""));
    }
}
