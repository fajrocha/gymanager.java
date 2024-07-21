package com.faroc.gymanager.application.sessions.gateways;

import com.faroc.gymanager.domain.sessions.Session;

public interface SessionsGateway {
    void create(Session session);
}
