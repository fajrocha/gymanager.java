package com.faroc.gymanager.gyms.requests;

import java.util.UUID;

public record GymResponse(UUID id, UUID subscriptionId, String name) {
}
