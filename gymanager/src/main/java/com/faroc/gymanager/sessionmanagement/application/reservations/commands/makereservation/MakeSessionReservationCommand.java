package com.faroc.gymanager.sessionmanagement.application.reservations.commands.makereservation;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.sessionmanagement.domain.sessions.SessionReservation;

import java.util.UUID;

public record MakeSessionReservationCommand(UUID sessionId, UUID participantId) implements Command<SessionReservation> {
}
