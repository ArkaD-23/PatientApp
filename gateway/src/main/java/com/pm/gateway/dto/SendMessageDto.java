package com.pm.gateway.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageDto {

    private String senderId;
    private String receiverId;
    private String roomId;
    private String content;

}

