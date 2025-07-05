package com.pm.auth_service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "users_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role")
    private String role;

    @Column(name = "username")
    private String username;

    @Column(name = "fullname", nullable = false)
    private String fullname;

    @Column(name = "creation_timestamp")
    @CreationTimestamp
    @JsonProperty("creation_timestamp")
    private Date creationTimestamp;

    @Column(name = "last_update_timestamp")
    @UpdateTimestamp
    @JsonProperty("last_update_timestamp")
    private Date lastUpdateTimestamp;
}
