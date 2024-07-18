package com.faroc.gymanager.application.rooms.gateways;

import com.faroc.gymanager.domain.rooms.Room;

import java.util.Optional;
import java.util.UUID;

public interface RoomsGateway {
    void create(Room room);
    Optional<Room> findById(UUID id);
}
