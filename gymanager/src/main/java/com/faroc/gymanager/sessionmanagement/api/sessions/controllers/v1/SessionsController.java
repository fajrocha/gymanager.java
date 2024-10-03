package com.faroc.gymanager.sessionmanagement.api.sessions.controllers.v1;

import an.awesome.pipelinr.Pipeline;
import com.faroc.gymanager.sessionmanagement.api.sessions.mappers.SessionRequestMappers;
import com.faroc.gymanager.sessionmanagement.api.sessions.mappers.SessionResponseMappers;
import com.faroc.gymanager.sessionmanagement.application.sessions.queries.getsession.FetchSessionQuery;
import com.faroc.gymanager.sessionmanagement.api.sessions.contracts.v1.requests.AddSessionRequest;
import com.faroc.gymanager.sessionmanagement.api.sessions.contracts.v1.responses.SessionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController("Sessions Controller V1")
@RequestMapping("v1/rooms/{roomId}/sessions")
@Tag(name = "Sessions")
public class SessionsController {

    private final Pipeline pipeline;

    public SessionsController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @PostMapping
    @Operation(
            summary = "Add session to a room.",
            description = "Trainer adds a session to a given room."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Session added to room.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SessionResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "User is not authenticated.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Room not free to add session at specified hour.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error or unexpected behavior like missing related records " +
                            "(trainer, room, gym)  during request to add session.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    })
    })
    public ResponseEntity<SessionResponse> addSession(
            @Parameter(description = "Room id to add session to.") @PathVariable UUID roomId,
            @Valid @RequestBody AddSessionRequest addSessionRequest) {
        var command = SessionRequestMappers.toCommand(addSessionRequest, roomId);

        var session = command.execute(pipeline);

        return new ResponseEntity<>(SessionResponseMappers.toResponse(session), HttpStatus.CREATED) ;
    }

    @Operation(
            summary = "Fetch a session.",
            description = "Fetch a session by id."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Session fetched.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SessionResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "User is not authenticated.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Session requested not found.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error or unexpected behavior like missing related records " +
                            "(room)  during request to add session.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    })
    })
    @GetMapping("{sessionId}")
    public SessionResponse fetchSession(
            @Parameter(description = "Room id of session to fetch.") @PathVariable UUID roomId,
            @Parameter(description = "Id of session to fetch.") @PathVariable UUID sessionId) {
        var query = new FetchSessionQuery(roomId, sessionId);

        var session = query.execute(pipeline);

        return SessionResponseMappers.toResponse(session);
    }
}
