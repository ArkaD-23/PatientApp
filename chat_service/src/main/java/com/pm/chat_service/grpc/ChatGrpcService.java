package com.pm.chat_service.grpc;

import com.pm.chat_service.model.ChatMessageEntity;
import com.pm.chat_service.service.ChatMessageService;
import com.pm.chatservice.grpc.ChatAck;
import com.pm.chatservice.grpc.ChatMessage;
import com.pm.chatservice.grpc.ChatServiceGrpc;
import com.pm.chatservice.grpc.StreamRequest;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@GrpcService
@RequiredArgsConstructor
public class ChatGrpcService extends ChatServiceGrpc.ChatServiceImplBase {

    private final ChatMessageService chatService;

    private final List<StreamObserver<ChatMessage>> subscribers = new CopyOnWriteArrayList<>();

    @Override
    public void sendMessage(ChatMessage request, StreamObserver<ChatAck> responseObserver) {
        // Save to DB
        ChatMessageEntity saved = chatService.save(
                request.getRoomId(),
                request.getFrom(),
                request.getText(),
                request.getType()
        );

        // Broadcast to gRPC subscribers
        ChatMessage protoMsg = ChatMessage.newBuilder()
                .setRoomId(saved.getRoomId())
                .setFrom(saved.getSender())
                .setText(saved.getText())
                .setType(saved.getType())
                .setTs(saved.getTs().toString())
                .build();

        for (StreamObserver<ChatMessage> sub : subscribers) {
            sub.onNext(protoMsg);
        }

        // Ack
        ChatAck ack = ChatAck.newBuilder().setStatus("SAVED").build();
        responseObserver.onNext(ack);
        responseObserver.onCompleted();
    }

    @Override
    public void streamMessages(StreamRequest request, StreamObserver<ChatMessage> responseObserver) {
        subscribers.add(responseObserver);
    }
}
