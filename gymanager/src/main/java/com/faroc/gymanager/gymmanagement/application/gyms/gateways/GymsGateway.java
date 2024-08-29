package com.faroc.gymanager.gymmanagement.application.gyms.gateways;

import com.faroc.gymanager.gymmanagement.domain.gyms.Gym;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GymsGateway {
    void save(Gym gym);
    void update(Gym gym);
    Optional<Gym> findById(UUID id);
    List<Gym> findBySubscriptionId(UUID subscriptionId);
    void delete(Gym gym);
    void deleteBySubscription(UUID subscriptionId);
}
