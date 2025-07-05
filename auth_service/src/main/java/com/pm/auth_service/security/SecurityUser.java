package com.pm.auth_service.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pm.auth_service.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SecurityUser extends AbstractUserAccount implements UserDetails {

    @JsonCreator
    public SecurityUser(@JsonProperty("user") User user) {
        this.user = user;
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(user.getRole().split(","))
                .map(String::trim)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @JsonIgnore
    public String getName() {
        return getUsername();
    }

}

