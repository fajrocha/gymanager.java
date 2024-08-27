package com.faroc.gymanager.gymmanagement.application.rooms.commands.addroom;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.gymmanagement.application.gyms.gateways.GymsGateway;
import com.faroc.gymanager.common.application.abstractions.DomainEventsPublisher;
import com.faroc.gymanager.gymmanagement.application.subscriptions.gateways.SubscriptionsGateway;
import com.faroc.gymanager.gymmanagement.domain.rooms.Room;
import com.faroc.gymanager.common.domain.exceptions.UnexpectedException;
import com.faroc.gymanager.gymmanagement.domain.rooms.errors.RoomErrors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AddRoomHandler implements Command.Handler<AddRoomCommand, Room> {
    private final GymsGateway gymsGateway;
    private final SubscriptionsGateway subscriptionsGateway;
    private final DomainEventsPublisher domainEventsHandler;

    @Autowired
    public AddRoomHandler(
            GymsGateway gymsGateway,
            SubscriptionsGateway subscriptionsGateway,
            DomainEventsPublisher domainEventsHandler
    ) {
        this.gymsGateway = gymsGateway;
        this.subscriptionsGateway = subscriptionsGateway;
        this.domainEventsHandler = domainEventsHandler;
    }

    @Override
    @Transactional
    public Room handle(AddRoomCommand addRoomCommand) {
        var gymId = addRoomCommand.gymId();

        var gym = gymsGateway.findById(gymId)
                .orElseThrow(() -> new UnexpectedException(
                        RoomErrors.gymNotFound(gymId),
                        RoomErrors.GYM_NOT_FOUND)
                );

        var subscriptionId = gym.getSubscriptionId();
        var subscription = subscriptionsGateway.findById(subscriptionId)
                .orElseThrow(() -> new UnexpectedException(
                        RoomErrors.subscriptionNotFound(subscriptionId),
                        RoomErrors.SUBSCRIPTION_NOT_FOUND
                ));

        var room = new Room(
                gymId,
                addRoomCommand.name(),
                subscription.getMaxDailySessions()
        );

        gym.addRoom(room);

        gymsGateway.update(gym);
        domainEventsHandler.publishEventsFromAggregate(gym);

        return room;
    }
}
