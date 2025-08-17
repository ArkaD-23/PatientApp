package com.pm.gateway.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {

    private String id;
    private String senderId;
    private String receiverId;
    private String roomId;
    private String content;
    private String timestamp;

}
