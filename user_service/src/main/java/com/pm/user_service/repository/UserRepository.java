package com.pm.user_service.repository;

import com.pm.user_service.model.User;
import com.pm.user_service.util.Status;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends MongoRepository<User, UUID> {

    User findByEmail(String email);

    boolean existsByEmail(String email);

    User findByUsername(String username);

    List<User> findAllByStatus(Status status);
}
