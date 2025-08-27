package com.pm.user_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ValidationDto {

    private String email;
    private String password;
}
