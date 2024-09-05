package com.faroc.gymanager.gymmanagement.api.subscriptions.contracts.v1.requests;

import com.faroc.gymanager.common.api.validation.ValidEnum;
import com.faroc.gymanager.gymmanagement.domain.subscriptions.SubscriptionType;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record SubscribeRequest(
        @ValidEnum(enumClass = SubscriptionType.class)
        @NotNull(message = "The subscription type must not be empty.")
        String subscriptionType,

        @NotNull(message = "The admin type must not be empty.")
        UUID adminId) {
}
