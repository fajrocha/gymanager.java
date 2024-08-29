package com.faroc.gymanager.gymmanagement.subscriptions.requests;

import com.faroc.gymanager.gymmanagement.subscriptions.shared.SubscriptionTypeApi;

import java.util.UUID;

public record SubscribeRequest(SubscriptionTypeApi subscriptionType, UUID adminId) {
}
