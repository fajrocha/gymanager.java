package com.faroc.gymanager.application.participants.events;

import com.faroc.gymanager.application.participants.gateways.ParticipantsGateway;
import com.faroc.gymanager.domain.participants.Participant;
import com.faroc.gymanager.domain.sessions.SessionReservation;
import com.faroc.gymanager.domain.sessions.Session;
import com.faroc.gymanager.domain.sessions.events.MakeReservationEvent;
import com.faroc.gymanager.domain.shared.exceptions.EventualConsistencyException;
import com.faroc.gymanager.domain.shared.exceptions.UnexpectedException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class MakeReservationParticipantEventHandler {
    private final ParticipantsGateway participantsGateway;

    public MakeReservationParticipantEventHandler(ParticipantsGateway participantsGateway) {
        this.participantsGateway = participantsGateway;
    }

    @Async
    @TransactionalEventListener
    public void handle(MakeReservationEvent event) {
        var session = event.session();
        var reservation = event.reservation();

        var participant = addToSchedule(session, reservation);

        try {
            participantsGateway.update(participant);
        } catch (Exception ex) {
            throw new EventualConsistencyException(
                    "Failed to update the participant with new reservation " + reservation.getId() + ".");
        }

    }

    private Participant addToSchedule(Session session, SessionReservation reservation) {
        try {
            var participantId = reservation.getParticipantId();
            var participant = participantsGateway.findById(participantId).orElseThrow(
                    () -> new UnexpectedException(
                            MakeReservationEvent.participantNotFound(
                                    participantId,
                                    reservation.getId(),
                                    session.getId()
                            )
                    )
            );

            participant.makeReservation(session);

            return participant;
        } catch (Exception ex) {
            throw new EventualConsistencyException(ex.getMessage(), ex.getCause());
        }
    }
}
