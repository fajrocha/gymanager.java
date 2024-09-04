package com.faroc.gymanager.gymmanagement.api.rooms.controllers.v1;

import an.awesome.pipelinr.Pipeline;
import com.faroc.gymanager.gymmanagement.api.rooms.mappers.RoomResponseMappers;
import com.faroc.gymanager.gymmanagement.application.rooms.commands.addroom.AddRoomCommand;
import com.faroc.gymanager.gymmanagement.api.rooms.contracts.v1.requests.AddRoomRequest;
import com.faroc.gymanager.gymmanagement.api.rooms.contracts.v1.responses.RoomResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController("Rooms Controller V1")
@RequestMapping("v1/gyms/{gymId}/rooms")
@Tag(name = "Rooms")
public class RoomsController {
    private final Pipeline pipeline;

    public RoomsController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @PostMapping
    public ResponseEntity<RoomResponse> addRoom(@PathVariable UUID gymId, @RequestBody AddRoomRequest addRoomRequest) {
        var command = new AddRoomCommand(gymId, addRoomRequest.name());

        var room = command.execute(pipeline);

        return new ResponseEntity<>(RoomResponseMappers.toResponse(room), HttpStatus.CREATED);
    }
}
