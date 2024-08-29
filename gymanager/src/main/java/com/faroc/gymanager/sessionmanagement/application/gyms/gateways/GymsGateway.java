package com.faroc.gymanager.sessionmanagement.application.gyms.gateways;

import com.faroc.gymanager.sessionmanagement.domain.gyms.Gym;

import java.util.Optional;
import java.util.UUID;

public interface GymsGateway {
    Optional<Gym> findById(UUID id);
    void updateSessions(Gym sessions);
}
