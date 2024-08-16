package com.faroc.gymanager.application.participants.events;

import com.faroc.gymanager.application.participants.gateways.ParticipantsGateway;
import com.faroc.gymanager.domain.participants.Participant;
import com.faroc.gymanager.domain.shared.exceptions.EventualConsistencyException;
import com.faroc.gymanager.domain.users.events.AddParticipantEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class AddParticipantEventHandler {
    private final ParticipantsGateway participantsGateway;

    @Autowired
    public AddParticipantEventHandler(ParticipantsGateway participantsGateway) {
        this.participantsGateway = participantsGateway;
    }

    @Async
    @TransactionalEventListener
    public void handle(AddParticipantEvent event) {
        var participantId = event.participantId();
        var userId = event.userId();

        var participant = new Participant(participantId, userId);

        try {
            participantsGateway.create(participant);
        } catch (Exception ex) {
            throw new EventualConsistencyException(
                    "Failed to add participant with id " + participantId + " to user with id" + userId + ".");
        }
    }

}
