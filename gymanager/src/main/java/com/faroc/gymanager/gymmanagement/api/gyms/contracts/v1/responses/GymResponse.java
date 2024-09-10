package com.faroc.gymanager.gymmanagement.api.gyms.contracts.v1.responses;

import java.util.List;
import java.util.UUID;

public record GymResponse(UUID id, String name, List<String> sessionCategories) {
}
