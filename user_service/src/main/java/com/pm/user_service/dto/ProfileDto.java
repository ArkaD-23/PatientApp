package com.pm.user_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Profile;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDto {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("fullname")
    private String fullname;

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonProperty("role")
    private String role;

    public ProfileDto(String fullname, String email, String password, String role) {

        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public ProfileDto(UUID id, String fullname, String email, String password) {

        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.password = password;
    }

}
