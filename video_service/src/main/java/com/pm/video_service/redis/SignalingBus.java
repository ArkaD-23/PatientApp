package com.pm.video_service.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pm.video_service.model.Room;
import com.pm.video_service.service.RoomRegistry;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

import jakarta.annotation.PostConstruct;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SignalingBus implements MessageListener {
    private static final Logger log = LoggerFactory.getLogger(SignalingBus.class);

    private final StringRedisTemplate redis;
    private final RedisMessageListenerContainer container;
    private final RoomRegistry rooms;
    private final ObjectMapper json = new ObjectMapper();

    @Value("${signaling.redisChannelPrefix:room}")
    private String prefix;

    @PostConstruct
    public void init() {
        // Wildcard subscription per room is not supported; subscribe programmatically as rooms are created in gRPC.
    }

    public void subscribeRoom(String roomId) {
        container.addMessageListener(this, ChannelTopic.of(prefix + ":" + roomId));
    }

    public void publish(String roomId, Map<String, Object> payload) {
        try {
            redis.convertAndSend(prefix + ":" + roomId, json.writeValueAsString(payload));
        }
        catch (Exception e) {
            log.warn("Publish failed", e);
        }
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {

            String channel = new String(message.getChannel());
            String roomId = channel.substring(channel.lastIndexOf(':') + 1);
            Map<?,?> msg = json.readValue(message.getBody(), Map.class);
            var room = rooms.get(roomId).orElse(null);
            if (room == null) return;
            var doctor = room.getDoctorSession();
            var patient = room.getPatientSession();
            var text = new TextMessage(json.writeValueAsString(msg));
            if (doctor != null && doctor.isOpen()) doctor.sendMessage(text);
            if (patient != null && patient.isOpen()) patient.sendMessage(text);

        } catch (Exception e) {

            log.warn("onMessage error", e);
        }
    }
}
