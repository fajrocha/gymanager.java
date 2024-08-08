package com.faroc.gymanager.application.sessions.commands.addsession;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.application.gyms.gateways.GymsGateway;
import com.faroc.gymanager.application.rooms.gateways.RoomsGateway;
import com.faroc.gymanager.application.shared.abstractions.DomainEventsPublisher;
import com.faroc.gymanager.application.trainers.gateways.TrainersGateway;
import com.faroc.gymanager.domain.sessions.Session;
import com.faroc.gymanager.domain.sessions.errors.SessionErrors;
import com.faroc.gymanager.domain.shared.exceptions.ConflictException;
import com.faroc.gymanager.domain.shared.exceptions.UnexpectedException;
import com.faroc.gymanager.domain.shared.valueobjects.timeslots.TimeSlot;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AddSessionHandler implements Command.Handler<AddSessionCommand, Session> {
    private final RoomsGateway roomsGateway;
    private final TrainersGateway trainersGateway;
    private final GymsGateway gymsGateway;
    private final DomainEventsPublisher publisher;

    public AddSessionHandler(
            RoomsGateway roomsGateway,
            TrainersGateway trainersGateway,
            GymsGateway gymsGateway,
            DomainEventsPublisher publisher) {
        this.roomsGateway = roomsGateway;
        this.trainersGateway = trainersGateway;
        this.gymsGateway = gymsGateway;
        this.publisher = publisher;
    }

    @Override
    @Transactional
    public Session handle(AddSessionCommand addSessionCommand) {
        var roomId = addSessionCommand.roomId();

        var room = roomsGateway.findById(roomId)
                .orElseThrow(() -> new UnexpectedException(
                        SessionErrors.roomNotFound(roomId),
                        SessionErrors.ROOM_NOT_FOUND
                ));

        var trainerId = addSessionCommand.trainerId();

        var trainer = trainersGateway.findById(trainerId)
                .orElseThrow(() -> new UnexpectedException(
                        SessionErrors.trainerNotFound(trainerId),
                        SessionErrors.TRAINER_NOT_FOUND
                ));

        var timeSlot = TimeSlot.fromInstants(addSessionCommand.startTime(), addSessionCommand.endTime());

        if (!trainer.isFree(timeSlot))
            throw new ConflictException(
                    SessionErrors.trainerScheduleConflict(trainerId, timeSlot),
                    SessionErrors.TRAINER_SCHEDULE_CONFLICT);

        var gymId = room.getGymId();
        var gym = gymsGateway.findById(gymId)
                .orElseThrow(() -> new UnexpectedException(
                        SessionErrors.gymNotFound(gymId),
                        SessionErrors.GYM_NOT_FOUND
                ));

        var categoryName = addSessionCommand.category();
        if (!gym.hasCategory(categoryName))
            throw new UnexpectedException(
                    SessionErrors.sessionCategoryNotFound(gymId),
                    SessionErrors.SESSION_CATEGORY_NOT_FOUND);

        var session = new Session(
                trainerId,
                roomId,
                timeSlot,
                addSessionCommand.name(),
                addSessionCommand.description(),
                addSessionCommand.category(),
                addSessionCommand.maxParticipants()
        );

        room.makeReservation(session);

        roomsGateway.update(room);
        publisher.publishEventsFromAggregate(room);

        return session;
    }
}
