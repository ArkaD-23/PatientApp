package com.pm.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {

    private String id;

    private String roomId;

    private String senderId;

    private String recipientId;

    private String content;

    private Date timestamp;
}