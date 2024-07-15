package com.faroc.gymanager.domain.gyms.events;

import com.faroc.gymanager.domain.rooms.Room;
import com.faroc.gymanager.domain.shared.events.DomainEvent;

public record AddRoomEvent(Room room) implements DomainEvent {
}
