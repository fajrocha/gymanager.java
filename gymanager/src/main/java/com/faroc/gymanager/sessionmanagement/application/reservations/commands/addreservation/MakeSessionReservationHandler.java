package com.faroc.gymanager.sessionmanagement.application.reservations.commands.addreservation;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.sessionmanagement.application.sessions.gateways.SessionsGateway;
import com.faroc.gymanager.common.application.abstractions.DomainEventsPublisher;
import com.faroc.gymanager.sessionmanagement.application.participants.gateways.ParticipantsGateway;
import com.faroc.gymanager.sessionmanagement.domain.sessions.SessionReservation;
import com.faroc.gymanager.sessionmanagement.domain.sessions.SessionErrors;
import com.faroc.gymanager.common.domain.exceptions.ConflictException;
import com.faroc.gymanager.common.domain.exceptions.UnexpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MakeSessionReservationHandler implements Command.Handler<MakeSessionReservationCommand, SessionReservation> {
    private final SessionsGateway sessionsGateway;
    private final ParticipantsGateway participantsGateway;
    private final DomainEventsPublisher domainEventsPublisher;

    @Autowired
    public MakeSessionReservationHandler(
            SessionsGateway sessionsGateway,
            ParticipantsGateway participantsGateway,
            DomainEventsPublisher domainEventsPublisher) {
        this.sessionsGateway = sessionsGateway;
        this.participantsGateway = participantsGateway;
        this.domainEventsPublisher = domainEventsPublisher;
    }

    @Override
    @Transactional
    public SessionReservation handle(MakeSessionReservationCommand command) {
        var sessionId = command.sessionId();
        var participantId = command.participantId();

        var session = sessionsGateway.findById(sessionId)
                .orElseThrow(() -> new UnexpectedException(
                        SessionErrors.notFoundForReservation(sessionId),
                        SessionErrors.NOT_FOUND_FOR_RESERVATION)
                );

        var participant = participantsGateway.findById(participantId)
                .orElseThrow(() -> new UnexpectedException(
                        SessionErrors.participantNotFoundToMakeReservation(sessionId, participantId),
                        SessionErrors.PARTICIPANT_NOT_FOUND_TO_MAKE_RESERVATION
                        )
                );

        if (!participant.isFree(session.getTimeSlot()))
            throw new ConflictException(
                    SessionErrors.participantNotFreeToMakeReservation(sessionId, participantId),
                    SessionErrors.PARTICIPANT_NOT_FREE_TO_MAKE_RESERVATION);

        var reservation = new SessionReservation(participantId);
        session.makeReservation(reservation);
        domainEventsPublisher.publishEventsFromAggregate(session);

        return reservation;
    }
}
