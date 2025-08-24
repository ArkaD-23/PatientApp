package com.pm.chat_service.chat;

import com.pm.chat_service.chatroom.ChatRoomRepository;
import com.pm.chat_service.chatroom.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository repository;
    private final ChatRoomService chatRoomService;
    private final ChatRoomRepository chatRoomRepository;

    public ChatMessage save(ChatMessage chatMessage) {
        boolean exists = chatRoomRepository.existsByRoomId(chatMessage.getSenderId()+"_"+chatMessage.getRecipientId());
        var roomId = chatRoomService
                .getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true)
                .orElseThrow();
        chatMessage.setRoomId(roomId);
        repository.save(chatMessage);
        return chatMessage;
    }

    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {

        boolean exists = chatRoomRepository.existsByRoomId(senderId+"_"+recipientId);
        var roomId = chatRoomService.getChatRoomId(senderId, recipientId, exists);

        String reversedRoomId = roomId
                .map(id -> {
                    String[] parts = id.split("_");
                    return (parts.length == 2) ? parts[1] + "_" + parts[0] : id;
                })
                .orElse(null);

        List<ChatMessage> messages = new ArrayList<>();

        roomId.ifPresent(id -> messages.addAll(repository.findByRoomId(id)));

        if (reversedRoomId != null) {
            messages.addAll(repository.findByRoomId(reversedRoomId));
        }

        messages.sort(Comparator.comparing(ChatMessage::getTimestamp));

        return messages;
    }
}
