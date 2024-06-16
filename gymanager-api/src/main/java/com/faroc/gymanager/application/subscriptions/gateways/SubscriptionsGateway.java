package com.faroc.gymanager.application.subscriptions.gateways;

import com.faroc.gymanager.domain.subscriptions.Subscription;

import java.util.Optional;
import java.util.UUID;

public interface SubscriptionsGateway {
    void save(Subscription subscription);
    Optional<Subscription> findById(UUID id);
    void delete(Subscription subscription);
}
