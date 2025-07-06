package com.pm.auth_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.UUID;

@Document(collection = "users_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private UUID id = UUID.randomUUID();

    private String email;

    private String password;

    private String role;

    private String username;

    private String fullname;

    @CreatedDate
    @JsonProperty("creation_timestamp")
    private Date creationTimestamp;

    @LastModifiedDate
    @JsonProperty("last_update_timestamp")
    private Date lastUpdateTimestamp;
}
