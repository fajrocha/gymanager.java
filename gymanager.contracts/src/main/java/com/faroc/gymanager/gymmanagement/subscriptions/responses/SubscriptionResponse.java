package com.faroc.gymanager.gymmanagement.subscriptions.responses;

import com.faroc.gymanager.gymmanagement.subscriptions.shared.SubscriptionTypeApi;

import java.util.UUID;

public record SubscriptionResponse(UUID id, SubscriptionTypeApi subscriptionType) {
}
