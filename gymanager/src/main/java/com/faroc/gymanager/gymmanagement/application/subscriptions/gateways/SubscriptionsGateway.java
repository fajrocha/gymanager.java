package com.faroc.gymanager.gymmanagement.application.subscriptions.gateways;

import com.faroc.gymanager.gymmanagement.domain.subscriptions.Subscription;

import java.util.Optional;
import java.util.UUID;

public interface SubscriptionsGateway {
    void save(Subscription subscription);
    void update(Subscription subscription);
    Optional<Subscription> findById(UUID id);
    void delete(UUID subscriptionId);
    void delete(Subscription subscription);
}
