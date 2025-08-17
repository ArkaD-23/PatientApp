package com.pm.video_service.model;

import lombok.*;
import org.springframework.web.socket.WebSocketSession;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Room {
    private String roomId;
    private String doctorId;
    private String patientId;
    private boolean active;
    private Instant createdAt;
    private transient WebSocketSession doctorSession;
    private transient WebSocketSession patientSession;
}
