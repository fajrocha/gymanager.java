package com.faroc.gymanager.application.gyms.events;

import com.faroc.gymanager.application.gyms.gateways.GymsGateway;
import com.faroc.gymanager.domain.gyms.Gym;
import com.faroc.gymanager.domain.rooms.events.SessionReservationEvent;
import com.faroc.gymanager.domain.shared.exceptions.EventualConsistencyException;
import com.faroc.gymanager.domain.shared.exceptions.UnexpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Component
public class AddSessionGymEventHandler {
    private final GymsGateway gymsGateway;

    @Autowired
    public AddSessionGymEventHandler(GymsGateway gymsGateway) {
        this.gymsGateway = gymsGateway;
    }

    @Async
    @TransactionalEventListener
    public void handle(SessionReservationEvent event) {
        var gymId = event.room().getGymId();
        var trainerId = event.session().getTrainerId();

        var gym = addTrainerToGym(gymId, trainerId);

        try {
            gymsGateway.update(gym);
        } catch (Exception ex) {
            throw new EventualConsistencyException(
                    "Failed to update gym " + gym.getId() + " with new trainer " + trainerId + " when adding " +
                            "new session " + event.session().getId() + ".");
        }
    }

    private Gym addTrainerToGym(UUID gymId, UUID trainerId) {
        try {
            var gym = gymsGateway.findById(gymId)
                    .orElseThrow(() -> new UnexpectedException(SessionReservationEvent.gymNotFound(gymId, trainerId)));

            if (!gym.hasTrainer(trainerId))
                gym.addTrainer(trainerId);

            return gym;
        } catch (Exception ex) {
            throw new EventualConsistencyException(ex.getMessage(), ex.getCause());
        }
    }
}
