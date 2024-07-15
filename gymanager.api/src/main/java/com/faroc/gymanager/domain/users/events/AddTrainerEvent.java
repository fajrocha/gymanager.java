package com.faroc.gymanager.domain.users.events;

import com.faroc.gymanager.domain.shared.events.DomainEvent;

import java.util.UUID;

public record AddTrainerEvent(UUID trainerId, UUID userId) implements DomainEvent {
}
