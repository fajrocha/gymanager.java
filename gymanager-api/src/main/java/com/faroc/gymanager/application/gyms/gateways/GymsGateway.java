package com.faroc.gymanager.application.gyms.gateways;

import com.faroc.gymanager.domain.gyms.Gym;

import java.util.Optional;
import java.util.UUID;

public interface GymsGateway {
    void save(Gym gym);
    Optional<Gym> findById(UUID id);
    void delete(Gym gym);
}
