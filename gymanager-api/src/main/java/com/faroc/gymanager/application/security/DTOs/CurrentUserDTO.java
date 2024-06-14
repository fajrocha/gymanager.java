package com.faroc.gymanager.application.security.DTOs;

import java.util.List;
import java.util.UUID;

public record CurrentUserDTO(UUID id, List<String> permissions, List<String> roles) {
}
