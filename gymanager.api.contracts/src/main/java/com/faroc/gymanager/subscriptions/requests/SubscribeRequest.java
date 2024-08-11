package com.faroc.gymanager.subscriptions.requests;

import com.faroc.gymanager.subscriptions.shared.SubscriptionTypeApi;

import java.util.UUID;

public record SubscribeRequest(SubscriptionTypeApi subscriptionType, UUID adminId) {
}
