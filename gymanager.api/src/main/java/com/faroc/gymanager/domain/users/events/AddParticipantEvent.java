package com.faroc.gymanager.domain.users.events;

import com.faroc.gymanager.domain.shared.events.DomainEvent;

import java.util.UUID;

public record AddParticipantEvent(UUID participantId, UUID userId) implements DomainEvent {
}
