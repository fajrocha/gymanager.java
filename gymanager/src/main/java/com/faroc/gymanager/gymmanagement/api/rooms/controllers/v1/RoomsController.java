package com.faroc.gymanager.gymmanagement.api.rooms.controllers.v1;

import an.awesome.pipelinr.Pipeline;
import com.faroc.gymanager.gymmanagement.api.rooms.mappers.RoomResponseMappers;
import com.faroc.gymanager.gymmanagement.application.rooms.commands.addroom.AddRoomCommand;
import com.faroc.gymanager.gymmanagement.api.rooms.contracts.v1.requests.AddRoomRequest;
import com.faroc.gymanager.gymmanagement.api.rooms.contracts.v1.responses.RoomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController("Rooms Controller V1")
@RequestMapping("v1/gyms/{gymId}/rooms")
@Tag(name = "Rooms", description = "Requests to manage gym rooms.")
public class RoomsController {
    private final Pipeline pipeline;

    public RoomsController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @PostMapping
    @Operation(
            summary = "Adds a room to a gym.",
            description = "Adds a room to a given gym."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Room added to gym.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RoomResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "User is not authenticated.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error or unexpected behavior like missing or inconsistent related " +
                            "records (subscription, gym) during request to add room.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    })
    })
    public ResponseEntity<RoomResponse> addRoom(@PathVariable UUID gymId, @RequestBody AddRoomRequest addRoomRequest) {
        var command = new AddRoomCommand(gymId, addRoomRequest.name());

        var room = command.execute(pipeline);

        return new ResponseEntity<>(RoomResponseMappers.toResponse(room), HttpStatus.CREATED);
    }
}
