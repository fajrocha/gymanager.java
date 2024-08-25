package com.faroc.gymanager.sessionmanagement.application.participants.events;

import com.faroc.gymanager.sessionmanagement.application.participants.gateways.ParticipantsGateway;
import com.faroc.gymanager.sessionmanagement.domain.participants.Participant;
import com.faroc.gymanager.sessionmanagement.domain.sessions.Session;
import com.faroc.gymanager.sessionmanagement.domain.sessions.SessionReservation;
import com.faroc.gymanager.sessionmanagement.domain.sessions.events.MakeReservationEvent;
import com.faroc.gymanager.common.domain.exceptions.EventualConsistencyException;
import com.faroc.gymanager.common.domain.exceptions.UnexpectedException;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
public class MakeReservationParticipantEventHandler {
    private final ParticipantsGateway participantsGateway;

    public MakeReservationParticipantEventHandler(ParticipantsGateway participantsGateway) {
        this.participantsGateway = participantsGateway;
    }

    @ApplicationModuleListener
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
