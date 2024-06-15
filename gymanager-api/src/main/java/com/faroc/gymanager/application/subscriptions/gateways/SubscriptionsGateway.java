package com.faroc.gymanager.application.subscriptions.gateways;

import com.faroc.gymanager.domain.subscriptions.Subscription;

public interface SubscriptionsGateway {
    void save(Subscription subscription);
}
