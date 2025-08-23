package com.pm.user_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pm.user_service.util.Status;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Document(collection = "users_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    private String id;

    private String email;

    private String password;

    private String role;

    private String fullname;

    private String username;

    @CreatedDate
    private Date creationTimestamp;

    @LastModifiedDate
    private Date lastUpdateTimestamp;
}
