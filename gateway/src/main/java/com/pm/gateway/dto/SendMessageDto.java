package com.pm.gateway.dto;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageDto {

    private String roomId;
    private String senderId;
    private String content;
    private String type;
    private String ts;
}

