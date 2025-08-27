package com.pm.auth_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginDto {

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;
}
