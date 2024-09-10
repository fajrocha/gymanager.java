package com.faroc.gymanager.gymmanagement.api.subscriptions.contracts.v1.responses;

import com.faroc.gymanager.gymmanagement.api.subscriptions.contracts.v1.common.SubscriptionTypeApi;

import java.util.UUID;

public record SubscriptionResponse(UUID id, SubscriptionTypeApi subscriptionType) {
}
