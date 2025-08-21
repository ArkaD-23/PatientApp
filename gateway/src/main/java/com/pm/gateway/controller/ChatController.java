package com.pm.gateway.controller;

import com.pm.chatservice.grpc.*;
import com.pm.gateway.dto.BooleanDto;
import com.pm.gateway.dto.ChatMessage;
import com.pm.gateway.dto.ProfileDto;
import com.pm.gateway.ws.chat.ChatNotification;
import com.pm.userservice.grpc.*;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import com.google.protobuf.Empty;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/v1/chat")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;

    @GrpcClient("chatService")
    private ChatServiceGrpc.ChatServiceBlockingStub chatServiceStub;

    @GrpcClient("userService")
    private UserServiceGrpc.UserServiceBlockingStub userServiceStub;

    @MessageMapping("/user.addUser")
    @SendTo("/user/public")
    public BooleanDto add(@Payload UUID userId) {

        UserIdRequest req = UserIdRequest.newBuilder()
                .setId(userId.toString())
                .build();

        BooleanResponse res = userServiceStub.addUser(req);

        return new BooleanDto(res.getStatus());
    }

    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/public")
    public BooleanDto disconnect(@Payload UUID userId) {

        UserIdRequest req = UserIdRequest.newBuilder()
                .setId(userId.toString())
                .build();

        BooleanResponse res = userServiceStub.disconnectUser(req);

        return new BooleanDto(res.getStatus());
    }

    @GetMapping("/users")
    public ResponseEntity<List<ProfileDto>> findConnectedUsers() {

        ProfileListResponse res = userServiceStub.getConnectedUsers(Empty.getDefaultInstance());

        List<ProfileDto> list = res.getProfilesList().stream()
                .map(profile -> new ProfileDto(
                        profile.getId(),
                        profile.getEmail(),
                        profile.getFullname(),
                        profile.getRole(),
                        profile.getSuccess(),
                        profile.getMessage()
                ))
                .toList();

        return ResponseEntity.ok(list);
    }

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {
        // build gRPC request
        SaveChatMessageRequest req = SaveChatMessageRequest.newBuilder()
                .setSenderId(chatMessage.getSenderId())
                .setRecipientId(chatMessage.getRecipientId())
                .setContent(chatMessage.getContent())
                .build();

        SaveChatMessageResponse savedMsg = chatServiceStub.saveMessage(req);

        messagingTemplate.convertAndSendToUser(
                savedMsg.getMessage().getRecipientId(), "/queue/messages",
                new ChatNotification(
                        savedMsg.getMessage().getId(),
                        savedMsg.getMessage().getSenderId(),
                        savedMsg.getMessage().getRecipientId(),
                        savedMsg.getMessage().getContent()
                )
        );
    }

    /**
     * REST endpoint to fetch chat history
     */
    @GetMapping("/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(
            @PathVariable String senderId,
            @PathVariable String recipientId) {

        // build request
        GetChatMessagesRequest req = GetChatMessagesRequest.newBuilder()
                        .setSenderId(senderId)
                        .setRecipientId(recipientId)
                        .build();

        // fetch response
        GetChatMessagesResponse res = chatServiceStub.getMessages(req);
        System.out.println("Response in chatcontroller: " + res);

        // map gRPC messages to DTOs
        List<ChatMessage> list = res.getMessagesList().stream()
                .map(m -> new ChatMessage(
                        m.getId(),
                        m.getRoomId(),
                        m.getSenderId(),
                        m.getRecipientId(),
                        m.getContent(),
                        new Date(m.getTimestamp())
                ))
                .collect(Collectors.toList());

        System.out.println("List response: " + list);

        return ResponseEntity.ok(list);
    }
}