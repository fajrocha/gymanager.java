package com.faroc.gymanager.usermanagement.application.users.gateways;

import com.faroc.gymanager.usermanagement.domain.users.User;

import java.util.Optional;
import java.util.UUID;

public interface UsersGateway {
    Optional<User> findById(UUID userId);
    Optional<User> findByEmail(String email);
    boolean emailExists(String email);
    void create(User user);
    void update(User user);
    void delete(UUID id);
}
