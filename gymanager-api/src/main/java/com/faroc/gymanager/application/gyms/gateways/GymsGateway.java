package com.faroc.gymanager.application.gyms.gateways;

import com.faroc.gymanager.domain.gyms.Gym;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GymsGateway {
    void save(Gym gym);
    Optional<Gym> findById(UUID id);
    List<Gym> findBySubscriptionId(UUID subscriptionId);
    void delete(Gym gym);
    void deleteBySubscriptionId(UUID subscriptionId);
}
