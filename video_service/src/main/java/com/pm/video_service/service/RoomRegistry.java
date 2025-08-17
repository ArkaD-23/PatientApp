package com.pm.video_service.service;

import com.pm.video_service.model.Room;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RoomRegistry {
    private final Map<String, Room> rooms = new ConcurrentHashMap<>();

    public Room create(String doctorId, String patientId) {
        Room r = Room.builder()
                .roomId(UUID.randomUUID().toString())
                .doctorId(doctorId).patientId(patientId)
                .active(true).createdAt(Instant.now())
                .build();
        rooms.put(r.getRoomId(), r);
        return r;
    }

    public Optional<Room> get(String roomId) { return Optional.ofNullable(rooms.get(roomId)); }

    public boolean close(String roomId) {
        Room r = rooms.get(roomId);
        if (r == null) return false;
        r.setActive(false);
        return true;
    }
}
