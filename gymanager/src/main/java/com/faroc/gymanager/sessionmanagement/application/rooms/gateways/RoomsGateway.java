package com.faroc.gymanager.sessionmanagement.application.rooms.gateways;

import com.faroc.gymanager.sessionmanagement.domain.rooms.Room;

import java.util.Optional;
import java.util.UUID;

public interface RoomsGateway {
    void create(Room room);
    void update(Room room);
    Optional<Room> findById(UUID id);
}
