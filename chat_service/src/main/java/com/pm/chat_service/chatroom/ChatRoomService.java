package com.pm.chat_service.chatroom;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public Optional<String> getChatRoomId(
            String senderId,
            String recipientId,
            boolean createNewRoomIfNotExists
    ) {
        return chatRoomRepository
                .findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getRoomId)
                .or(() -> {
                    if(!createNewRoomIfNotExists) {
                        var roomId = createRoomId(senderId, recipientId);
                        System.out.println("Roomid: " + roomId);
                        return Optional.of(roomId);
                    }

                    return  Optional.empty();
                });
    }

    private String createRoomId(String senderId, String recipientId) {
        var chatId = String.format("%s_%s", senderId, recipientId);
        var reverseChatId = String.format("%s_%s", recipientId, senderId);

        ChatRoom senderRecipient = ChatRoom
                .builder()
                .roomId(chatId)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();

        ChatRoom recipientSender = ChatRoom
                .builder()
                .roomId(reverseChatId)
                .senderId(recipientId)
                .recipientId(senderId)
                .build();

        chatRoomRepository.save(senderRecipient);
        chatRoomRepository.save(recipientSender);

        return chatId;
    }
}