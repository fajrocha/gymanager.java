package com.faroc.gymanager.gymmanagement.application.gyms.events;

import com.faroc.gymanager.gymmanagement.application.gyms.gateways.GymsGateway;
import com.faroc.gymanager.gymmanagement.domain.gyms.Gym;
import com.faroc.gymanager.sessionmanagement.domain.rooms.events.SessionReservationEvent;
import com.faroc.gymanager.common.domain.exceptions.EventualConsistencyException;
import com.faroc.gymanager.common.domain.exceptions.UnexpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AddSessionTrainerToGymEventHandler {
    private final GymsGateway gymsGateway;

    @Autowired
    public AddSessionTrainerToGymEventHandler(GymsGateway gymsGateway) {
        this.gymsGateway = gymsGateway;
    }

    @ApplicationModuleListener
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
