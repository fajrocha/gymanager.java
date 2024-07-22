package com.faroc.gymanager.application.trainers.events;

import com.faroc.gymanager.application.users.gateways.TrainersGateway;
import com.faroc.gymanager.domain.rooms.events.SessionReservationEvent;
import com.faroc.gymanager.domain.sessions.Session;
import com.faroc.gymanager.domain.shared.exceptions.EventualConsistencyException;
import com.faroc.gymanager.domain.shared.exceptions.UnexpectedException;
import com.faroc.gymanager.domain.trainers.Trainer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Component
public class AddSessionTrainerEventHandler {
    private final TrainersGateway trainersGateway;

    public AddSessionTrainerEventHandler(TrainersGateway trainersGateway) {
        this.trainersGateway = trainersGateway;
    }

    @Async
    @TransactionalEventListener
    public void handle(SessionReservationEvent sessionReservationEvent) {
        var session = sessionReservationEvent.session();
        var trainerId = session.getTrainerId();

        var trainer = makeSessionReservation(trainerId, session);

        try {
            trainersGateway.update(trainer);
        } catch (Exception ex) {
            throw new EventualConsistencyException(
                    SessionReservationEvent.trainerUpdateFailed(trainerId, session.getId()),
                    ex.getCause());
        }
    }

    private Trainer makeSessionReservation(UUID trainerId, Session session) {
        try {
            var trainer = trainersGateway.findById(trainerId)
                    .orElseThrow(() -> new UnexpectedException(
                            SessionReservationEvent.trainerNotFound(trainerId, session.getId()))
                    );

            trainer.makeReservation(session);

            return trainer;
        } catch (Exception ex) {
             throw new EventualConsistencyException(ex.getMessage(), ex.getCause());
        }
    }
}
