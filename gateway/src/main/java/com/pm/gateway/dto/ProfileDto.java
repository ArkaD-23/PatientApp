package com.pm.gateway.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProfileDto {

    private String id;
    private String fullname;
    private String email;
    private String role;
    private String username;
}

