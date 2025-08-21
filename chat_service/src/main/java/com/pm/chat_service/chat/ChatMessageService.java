package com.pm.chat_service.chat;

import com.pm.chat_service.chatroom.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository repository;
    private final ChatRoomService chatRoomService;

    public ChatMessage save(ChatMessage chatMessage) {
        var roomId = chatRoomService
                .getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true)
                .orElseThrow();
        chatMessage.setRoomId(roomId);
        repository.save(chatMessage);
        return chatMessage;
    }

    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {

        var roomId = chatRoomService.getChatRoomId(senderId, recipientId, false);

        System.out.println("roomId: " + roomId);

        return roomId.map(repository::findByRoomId).orElse(new ArrayList<>());
    }
}
