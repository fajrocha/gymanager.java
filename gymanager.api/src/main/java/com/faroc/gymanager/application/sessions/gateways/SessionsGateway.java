package com.faroc.gymanager.application.sessions.gateways;

import com.faroc.gymanager.domain.sessions.Session;

import java.util.Optional;
import java.util.UUID;

public interface SessionsGateway {
    void create(Session session);
    Optional<Session> findById(UUID id);
}
