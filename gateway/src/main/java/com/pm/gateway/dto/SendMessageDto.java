package com.pm.gateway.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageDto {

    private String id;
    private String roomId;
    private String senderId;
    private String content;
    private String type;
    private Long ts;
}

