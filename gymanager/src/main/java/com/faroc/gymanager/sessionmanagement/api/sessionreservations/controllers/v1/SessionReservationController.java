package com.faroc.gymanager.sessionmanagement.api.sessionreservations.controllers.v1;

import an.awesome.pipelinr.Pipeline;
import com.faroc.gymanager.sessionmanagement.application.reservations.commands.makereservation.MakeSessionReservationCommand;
import com.faroc.gymanager.sessionmanagement.api.sessionreservations.contracts.v1.requests.MakeSessionReservationRequest;
import com.faroc.gymanager.sessionmanagement.api.sessionreservations.contracts.v1.responses.MakeSessionReservationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("v1/sessions/{sessionId}/reservations")
@Tag(name = "Session Reservations", description = "Requests to manage session reservations.")
public class SessionReservationController {
    private final Pipeline pipeline;

    @Autowired
    public SessionReservationController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @PostMapping
    @Operation(
            summary = "Session reservation by a participant.",
            description = "Participant makes reservation on a given session."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Session reservation made by participant.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MakeSessionReservationResponse.class)
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
                    description = "Participant not free to make the reservation.",
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
                            "(session, participant)  during request to make session reservation.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    })
    })
    public MakeSessionReservationResponse makeReservation(
            @Parameter(description = "Session id to make reservation on.") @PathVariable UUID sessionId,
            @RequestBody MakeSessionReservationRequest request) {
        var command = new MakeSessionReservationCommand(sessionId, request.participantId());

        var sessionReservation = command.execute(pipeline);

        return new MakeSessionReservationResponse(sessionReservation.getId(), sessionReservation.getParticipantId());
    }
}
