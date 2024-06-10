package com.faroc.gymanager.application.users.gateways;

import com.faroc.gymanager.domain.users.User;

import java.util.Optional;
import java.util.UUID;

public interface UsersGateway {
    Optional<User> findById(UUID userId);
    Optional<User> findByEmail(String email);
    boolean emailExists(String email);
    int save(User user);
    int update(User user);
}
