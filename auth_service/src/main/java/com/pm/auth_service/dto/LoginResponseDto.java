package com.pm.auth_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginResponseDto {

    private Boolean status;
    private String token;
}