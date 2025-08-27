package com.pm.gateway.dto;

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
