package com.faroc.gymanager.sessionmanagement.application.trainers.events;

import com.faroc.gymanager.sessionmanagement.application.trainers.gateways.TrainersGateway;
import com.faroc.gymanager.common.domain.exceptions.EventualConsistencyException;
import com.faroc.gymanager.sessionmanagement.domain.trainers.Trainer;
import com.faroc.gymanager.usermanagement.domain.users.events.AddTrainerEvent;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
public class AddTrainerEventHandler {
    private final TrainersGateway trainersGateway;

    public AddTrainerEventHandler(TrainersGateway trainersGateway) {
        this.trainersGateway = trainersGateway;
    }

    @ApplicationModuleListener
    public void handle(AddTrainerEvent addTrainerEvent) {
        var trainerId = addTrainerEvent.trainerId();
        var userId = addTrainerEvent.userId();
        var admin = new Trainer(trainerId, userId);

        try {
            trainersGateway.create(admin);
        } catch (Exception ex) {
            throw new EventualConsistencyException(
                    "Failed to add trainer with id " + trainerId + " to user with id" + userId + ".");
        }
    }
}
