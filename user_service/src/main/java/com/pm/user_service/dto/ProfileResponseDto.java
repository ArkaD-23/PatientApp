package com.pm.user_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pm.user_service.model.User;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfileResponseDto {

    @JsonProperty("id")
    private String id;

    @JsonProperty("fullname")
    private String fullname;

    @JsonProperty("email")
    private String email;

    @JsonProperty("role")
    private String role;

    @JsonProperty("username")
    private String username;

    public ProfileResponseDto(User user) {
        this.id = user.getId();
        this.fullname = user.getFullname();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.username = user.getUsername();
    }
}
