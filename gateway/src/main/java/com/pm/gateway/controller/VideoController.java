package com.pm.gateway.controller;

import com.pm.gateway.dto.WebRTCMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class VideoController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/webrtc.offer")
    public void handleOffer(WebRTCMessage message) {
        System.out.println("📩 OFFER received: " + message.getType());
        messagingTemplate.convertAndSend(
                "/topic/webrtc." + message.getRecipientId(),
                message
        );
    }

    @MessageMapping("/webrtc.answer")
    public void handleAnswer(WebRTCMessage message) {
        messagingTemplate.convertAndSend(
                "/topic/webrtc." + message.getRecipientId(),
                message
        );
    }

    @MessageMapping("/webrtc.ice")
    public void handleIceCandidate(WebRTCMessage message) {
        messagingTemplate.convertAndSend(
                "/topic/webrtc." + message.getRecipientId(),
                message
        );
    }
}
