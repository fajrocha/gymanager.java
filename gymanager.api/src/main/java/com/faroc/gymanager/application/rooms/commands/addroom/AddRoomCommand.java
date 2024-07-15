package com.faroc.gymanager.application.rooms.commands.addroom;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.domain.rooms.Room;

import java.util.UUID;

public record AddRoomCommand(UUID gymId, String name) implements Command<Room> {
}
