package com.pm.video_service.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pm.video_service.model.Role;
import com.pm.video_service.model.Room;
import com.pm.video_service.service.RoomRegistry;
import com.pm.video_service.redis.SignalingBus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger; import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.util.*;

@Component
@RequiredArgsConstructor
public class SignalingHandler extends TextWebSocketHandler {
    private static final Logger log = LoggerFactory.getLogger(SignalingHandler.class);
    private final RoomRegistry rooms;
    private final SignalingBus bus;
    private final ObjectMapper json = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Map<String, String> q = parseQuery(session.getUri());
        String roomId = q.get("roomId");
        String userId = q.get("userId");
        Role role = Role.valueOf(q.getOrDefault("role", "PATIENT"));

        Room room = rooms.get(roomId).orElse(null);
        if (room == null || !room.isActive()) { session.close(new CloseStatus(4404, "Room not found")); return; }

        // 1:1 enforcement
        if (role == Role.DOCTOR) {
            if (room.getDoctorSession() != null && room.getDoctorSession().isOpen()) { session.close(new CloseStatus(4409, "Doctor already joined")); return; }
            if (room.getDoctorId() != null && !room.getDoctorId().equals(userId)) { session.close(new CloseStatus(4403, "Forbidden doctor")); return; }
            room.setDoctorSession(session);
        } else {
            if (room.getPatientSession() != null && room.getPatientSession().isOpen()) { session.close(new CloseStatus(4409, "Patient already joined")); return; }
            if (room.getPatientId() != null && !room.getPatientId().equals(userId)) { session.close(new CloseStatus(4403, "Forbidden patient")); return; }
            room.setPatientSession(session);
        }

        send(session, Map.of("type","READY","roomId", roomId, "role", role.name()));
        WebSocketSession other = otherPeer(room, session);
        if (other != null && other.isOpen()) {
            send(session, Map.of("type","PEER_JOINED"));
            send(other, Map.of("type","PEER_JOINED"));
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Map<String, Object> msg = json.readValue(message.getPayload(), Map.class);
        String roomId = Objects.toString(msg.get("roomId"), null);
        if (roomId == null) return;
        // publish to Redis channel; subscribers will fanout to the peer
        bus.publish(roomId, msg);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        rooms.get("noop"); // no-op; in minimal version we don't clean lookup maps by session id
    }

    private static Map<String, String> parseQuery(URI uri) {
        Map<String,String> m = new HashMap<>();
        if (uri == null || uri.getQuery() == null) return m;
        for (String p : uri.getQuery().split("&")) {
            String[] kv = p.split("=",2);
            if (kv.length==2) m.put(kv[0], java.net.URLDecoder.decode(kv[1], java.nio.charset.StandardCharsets.UTF_8));
        }
        return m;
    }

    private static void send(WebSocketSession s, Object o) throws Exception { s.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(o))); }
    private static WebSocketSession otherPeer(Room r, WebSocketSession me) {
        if (r.getDoctorSession() != null && !r.getDoctorSession().getId().equals(me.getId())) return r.getDoctorSession();
        if (r.getPatientSession() != null && !r.getPatientSession().getId().equals(me.getId())) return r.getPatientSession();
        return null;
    }
}
