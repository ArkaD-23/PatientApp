package com.pm.auth_service.security;

import com.pm.auth_service.model.User;

public class AbstractUserAccount  implements UserAccount {

    User user;

    @Override
    public User getUser() {
        return user;
    }
}

