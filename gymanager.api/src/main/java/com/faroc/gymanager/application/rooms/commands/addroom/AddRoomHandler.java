package com.faroc.gymanager.application.rooms.commands.addroom;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.application.gyms.gateways.GymsGateway;
import com.faroc.gymanager.application.shared.abstractions.DomainEventsHandler;
import com.faroc.gymanager.application.subscriptions.gateways.SubscriptionsGateway;
import com.faroc.gymanager.domain.rooms.Room;
import com.faroc.gymanager.domain.rooms.errors.RoomErrors;
import com.faroc.gymanager.domain.shared.exceptions.UnexpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AddRoomHandler implements Command.Handler<AddRoomCommand, Room> {
    private final GymsGateway gymsGateway;
    private final SubscriptionsGateway subscriptionsGateway;
    private final DomainEventsHandler domainEventsHandler;

    @Autowired
    public AddRoomHandler(
            GymsGateway gymsGateway,
            SubscriptionsGateway subscriptionsGateway,
            DomainEventsHandler domainEventsHandler
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
        domainEventsHandler.publishEvents(gym);

        return room;
    }
}
