package com.faroc.gymanager.sessionmanagement.api.sessionreservations.controllers.v1;

import an.awesome.pipelinr.Pipeline;
import com.faroc.gymanager.sessionmanagement.application.reservations.commands.makereservation.MakeSessionReservationCommand;
import com.faroc.gymanager.sessionmanagement.api.sessionreservations.contracts.v1.requests.MakeSessionReservationRequest;
import com.faroc.gymanager.sessionmanagement.api.sessionreservations.contracts.v1.responses.MakeSessionReservationResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("v1/sessions/{sessionId}/reservations")
@Tag(name = "Session Reservations")
public class SessionReservationController {
    private final Pipeline pipeline;

    @Autowired
    public SessionReservationController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @PostMapping
    public MakeSessionReservationResponse makeReservation(
            @PathVariable UUID sessionId,
            @RequestBody MakeSessionReservationRequest request) {
        var command = new MakeSessionReservationCommand(sessionId, request.participantId());

        var sessionReservation = command.execute(pipeline);

        return new MakeSessionReservationResponse(sessionReservation.getId(), sessionReservation.getParticipantId());
    }
}
