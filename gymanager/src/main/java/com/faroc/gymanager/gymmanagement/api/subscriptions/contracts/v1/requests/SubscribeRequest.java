package com.faroc.gymanager.gymmanagement.api.subscriptions.contracts.v1.requests;

import com.faroc.gymanager.gymmanagement.api.subscriptions.contracts.v1.common.SubscriptionTypeApi;

import java.util.UUID;

public record SubscribeRequest(SubscriptionTypeApi subscriptionType, UUID adminId) {
}
