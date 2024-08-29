package com.faroc.gymanager.gymmanagement.application.rooms.commands.addroom;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.gymmanagement.domain.rooms.RoomGym;

import java.util.UUID;

public record AddRoomCommand(UUID gymId, String name) implements Command<RoomGym> {
}
