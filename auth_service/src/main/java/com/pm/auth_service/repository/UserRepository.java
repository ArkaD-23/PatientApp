package com.pm.auth_service.repository;

import com.pm.auth_service.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface UserRepository extends MongoRepository<User, UUID> {

    User findByEmail(String email);

    boolean existsByEmail(String email);

    User findByUsername(String username);
}
