package com.faroc.gymanager.application.reservations.commands.addreservation;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.domain.sessions.SessionReservation;

import java.util.UUID;

public record MakeSessionReservationCommand(UUID sessionId, UUID participantId) implements Command<SessionReservation> {
}
