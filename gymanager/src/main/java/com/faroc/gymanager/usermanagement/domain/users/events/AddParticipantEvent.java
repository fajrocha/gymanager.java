package com.faroc.gymanager.usermanagement.domain.users.events;

import com.faroc.gymanager.common.domain.events.DomainEvent;

import java.util.UUID;

public record AddParticipantEvent(UUID participantId, UUID userId) implements DomainEvent {
}
