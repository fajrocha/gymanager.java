package com.faroc.gymanager.gymmanagement.domain.gyms.events;

import com.faroc.gymanager.common.domain.events.DomainEvent;
import com.faroc.gymanager.gymmanagement.domain.rooms.RoomGym;

public record AddRoomEvent(RoomGym room) implements DomainEvent {
}
