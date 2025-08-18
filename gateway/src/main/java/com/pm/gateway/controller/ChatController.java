package com.pm.gateway.controller;

import com.pm.gateway.dto.*;
import com.pm.chatservice.grpc.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@RestController
@RequestMapping("/v1/chat")
public class ChatController {

    @GrpcClient("chatService")
    private ChatServiceGrpc.ChatServiceBlockingStub chatServiceStub;

    @GrpcClient("chatService")
    private ChatServiceGrpc.ChatServiceStub chatServiceAsyncStub;

    // ✅ Send a message
    @PostMapping("/send")
    public ResponseEntity<BooleanDto> sendMessage(@RequestBody SendMessageDto dto) {
        ChatMessage req = ChatMessage.newBuilder()
                .setRoomId(dto.getRoomId())
                .setFrom(dto.getSenderId())
                .setText(dto.getContent())
                .setType(dto.getType())         // assuming type is in SendMessageDto
                .setTs(dto.getTs())      // assuming timestamp is in SendMessageDto
                .build();

        ChatAck res = chatServiceStub.sendMessage(req);

        // Return true if status == "SAVED"
        return ResponseEntity.ok(new BooleanDto("SAVED".equals(res.getStatus())));
    }

    // ✅ Stream messages from a room
    @GetMapping("/stream/{roomId}")
    public ResponseEntity<MessagesResponseDto> streamMessages(@PathVariable String roomId) throws InterruptedException {
        StreamRequest req = StreamRequest.newBuilder()
                .setRoomId(roomId)
                .build();

        List<MessageDto> messages = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);

        chatServiceAsyncStub.streamMessages(req, new io.grpc.stub.StreamObserver<ChatMessage>() {
            @Override
            public void onNext(ChatMessage m) {
                messages.add(new MessageDto(
                        null,                  // no explicit id in proto
                        m.getFrom(),
                        null,                  // no receiver field in proto
                        m.getRoomId(),
                        m.getText(),
                        m.getTs()
                ));
            }

            @Override
            public void onError(Throwable t) {
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                latch.countDown();
            }
        });

        // ⚠️ This will wait for the stream to complete (not ideal for long-lived streams)
        latch.await();

        return ResponseEntity.ok(new MessagesResponseDto(messages));
    }
}
