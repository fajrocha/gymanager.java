package com.faroc.gymanager.sessionmanagement.application.rooms.events;

import com.faroc.gymanager.sessionmanagement.domain.rooms.Room;
import com.faroc.gymanager.sessionmanagement.application.rooms.gateways.RoomsGateway;
import com.faroc.gymanager.gymmanagement.domain.gyms.events.AddRoomEvent;
import com.faroc.gymanager.common.domain.exceptions.EventualConsistencyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
public class AddRoomEventHandler {
    private final RoomsGateway roomsGateway;

    @Autowired
    public AddRoomEventHandler(RoomsGateway roomsGateway) {
        this.roomsGateway = roomsGateway;
    }

    @ApplicationModuleListener
    public void handle(AddRoomEvent addRoomEvent) {
        var room =  new Room(
                addRoomEvent.room().getId(),
                addRoomEvent.room().getGymId(),
                addRoomEvent.room().getName(),
                addRoomEvent.room().getMaxDailySessions()
        );

        try {
            roomsGateway.create(room);
        } catch (Exception ex) {
            throw new EventualConsistencyException("Failed to add room with id " + room.getId());
        }
    }
}
