package com.faroc.gymanager.sessionmanagement.application.sessions.commands.addsession;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.sessionmanagement.application.rooms.gateways.RoomsGateway;
import com.faroc.gymanager.common.application.abstractions.DomainEventsPublisher;
import com.faroc.gymanager.sessionmanagement.application.sessions.gateways.SessionCategoriesGateway;
import com.faroc.gymanager.sessionmanagement.application.trainers.gateways.TrainersGateway;
import com.faroc.gymanager.sessionmanagement.domain.sessions.Session;
import com.faroc.gymanager.common.domain.exceptions.ConflictException;
import com.faroc.gymanager.common.domain.exceptions.UnexpectedException;
import com.faroc.gymanager.sessionmanagement.domain.common.timeslots.TimeSlot;
import com.faroc.gymanager.sessionmanagement.domain.sessions.errors.SessionErrors;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AddSessionHandler implements Command.Handler<AddSessionCommand, Session> {
    private final RoomsGateway roomsGateway;
    private final TrainersGateway trainersGateway;
    private final SessionCategoriesGateway sessionCategoriesGateway;
    private final DomainEventsPublisher publisher;

    public AddSessionHandler(
            RoomsGateway roomsGateway,
            TrainersGateway trainersGateway,
            SessionCategoriesGateway sessionCategoriesGateway,
            DomainEventsPublisher publisher) {
        this.roomsGateway = roomsGateway;
        this.trainersGateway = trainersGateway;
        this.sessionCategoriesGateway = sessionCategoriesGateway;
        this.publisher = publisher;
    }

    @Override
    @Transactional
    public Session handle(AddSessionCommand addSessionCommand) {
        var roomId = addSessionCommand.roomId();

        var room = roomsGateway.findById(roomId)
                .orElseThrow(() -> new UnexpectedException(
                        SessionErrors.addSessionRoomNotFound(roomId),
                        SessionErrors.ADD_SESSION_ROOM_NOT_FOUND
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

        var categoryName = addSessionCommand.category();

        if (!sessionCategoriesGateway.existsByName(categoryName))
            throw new UnexpectedException(
                    SessionErrors.sessionCategoryNotSupported(),
                    SessionErrors.SESSION_CATEGORY_NOT_SUPPORTED);

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
