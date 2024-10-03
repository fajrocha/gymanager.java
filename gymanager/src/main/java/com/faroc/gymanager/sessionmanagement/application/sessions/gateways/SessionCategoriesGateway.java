package com.faroc.gymanager.sessionmanagement.application.sessions.gateways;

import com.faroc.gymanager.sessionmanagement.domain.sessions.SessionCategory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SessionCategoriesGateway {
    void createAll(List<SessionCategory> sessionCategories);
    void exceptExisting(List<String> sessionCategories);
    boolean existsByName(String name);
    Optional<SessionCategory> findByName(String name);
    boolean exists(UUID id);
    boolean existsAll(List<UUID> id);
    void deleteAll(List<UUID> ids);
}
