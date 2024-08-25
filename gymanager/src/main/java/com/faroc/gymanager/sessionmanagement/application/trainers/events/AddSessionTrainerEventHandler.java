package com.faroc.gymanager.sessionmanagement.application.trainers.events;

import com.faroc.gymanager.sessionmanagement.application.trainers.gateways.TrainersGateway;
import com.faroc.gymanager.sessionmanagement.domain.rooms.events.SessionReservationEvent;
import com.faroc.gymanager.sessionmanagement.domain.sessions.Session;
import com.faroc.gymanager.common.domain.exceptions.EventualConsistencyException;
import com.faroc.gymanager.common.domain.exceptions.UnexpectedException;
import com.faroc.gymanager.sessionmanagement.domain.trainers.Trainer;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AddSessionTrainerEventHandler {
    private final TrainersGateway trainersGateway;

    public AddSessionTrainerEventHandler(TrainersGateway trainersGateway) {
        this.trainersGateway = trainersGateway;
    }

    @ApplicationModuleListener
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
