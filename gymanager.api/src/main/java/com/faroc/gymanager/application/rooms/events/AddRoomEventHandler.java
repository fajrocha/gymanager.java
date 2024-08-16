package com.faroc.gymanager.application.rooms.events;

import com.faroc.gymanager.application.rooms.gateways.RoomsGateway;
import com.faroc.gymanager.domain.gyms.events.AddRoomEvent;
import com.faroc.gymanager.domain.shared.exceptions.EventualConsistencyException;
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
        var room = addRoomEvent.room();

        try {
            roomsGateway.create(room);
        } catch (Exception ex) {
            throw new EventualConsistencyException("Failed to add room with id " + room.getId());
        }
    }
}
