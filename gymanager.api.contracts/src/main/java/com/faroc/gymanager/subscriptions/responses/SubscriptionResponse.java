package com.faroc.gymanager.subscriptions.responses;

import com.faroc.gymanager.subscriptions.shared.SubscriptionTypeApi;
import java.util.UUID;

public record SubscriptionResponse(UUID id, SubscriptionTypeApi subscriptionType) {
}
