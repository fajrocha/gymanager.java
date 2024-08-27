package com.faroc.gymanager.gymmanagement.gyms.responses;

import java.util.List;
import java.util.UUID;

public record GymResponse(UUID id, String name, List<String> sessionCategories) {
}
