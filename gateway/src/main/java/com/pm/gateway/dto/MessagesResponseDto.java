package com.pm.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MessagesResponseDto {
    private List<MessageDto> messages;
}
