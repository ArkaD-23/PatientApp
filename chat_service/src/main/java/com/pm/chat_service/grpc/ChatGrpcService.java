package com.pm.chat_service.grpc;

import com.pm.chat_service.chat.ChatMessageService;
import com.pm.chat_service.chatroom.ChatRoomService;
import com.pm.chatservice.grpc.*;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import com.pm.chat_service.chat.ChatMessage;

import java.util.stream.Collectors;

@GrpcService
@RequiredArgsConstructor
public class ChatGrpcService extends ChatServiceGrpc.ChatServiceImplBase {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    @Override
    public void saveMessage(SaveChatMessageRequest request,
                            StreamObserver<SaveChatMessageResponse> responseObserver) {
        try {
            ChatMessage message = new ChatMessage();
            message.setSenderId(request.getSenderId());
            message.setRecipientId(request.getRecipientId());
            message.setContent(request.getContent());

            ChatMessage saved = chatMessageService.save(message);

            SaveChatMessageResponse response = SaveChatMessageResponse.newBuilder()
                    .setMessage(com.pm.chatservice.grpc.ChatMessage.newBuilder()
                            .setId(saved.getId().toString())
                            .setRoomId(saved.getRoomId())
                            .setSenderId(saved.getSenderId())
                            .setRecipientId(saved.getRecipientId())
                            .setContent(saved.getContent())
                            .setTimestamp(saved.getTimestamp().getTime())
                            .build()
                    )
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void getMessages(GetChatMessagesRequest request,
                            StreamObserver<GetChatMessagesResponse> responseObserver) {
        try {
            var messages = chatMessageService.findChatMessages(
                    request.getSenderId(),
                    request.getRecipientId()
            );

            System.out.println("Response in chatgrpcservice: " + messages);

            GetChatMessagesResponse response = GetChatMessagesResponse.newBuilder()
                    .addAllMessages(messages.stream().map(m ->
                            com.pm.chatservice.grpc.ChatMessage.newBuilder()
                                    .setId(m.getId().toString())
                                    .setRoomId(m.getRoomId())
                                    .setSenderId(m.getSenderId())
                                    .setRecipientId(m.getRecipientId())
                                    .setContent(m.getContent())
                                    .setTimestamp(m.getTimestamp().getTime())
                                    .build()
                    ).collect(Collectors.toList()))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    public void getChatRoomId(ChatRoomRequest request, StreamObserver<ChatRoomResponse> responseObserver) {
        try {
            var chatIdOpt = chatRoomService.getChatRoomId(
                    request.getSenderId(),
                    request.getRecipientId(),
                    request.getCreateIfNotExists()
            );

            ChatRoomResponse response = ChatRoomResponse.newBuilder()
                    .setChatId(chatIdOpt.orElse(""))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }
}

