package com.faroc.gymanager.sessionmanagement.api.sessionreservations;

import an.awesome.pipelinr.Pipeline;
import com.faroc.gymanager.sessionmanagement.application.reservations.commands.addreservation.MakeSessionReservationCommand;
import com.faroc.gymanager.sessionmanagement.reservations.requests.MakeSessionReservationRequest;
import com.faroc.gymanager.sessionmanagement.reservations.responses.MakeSessionReservationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("sessions/{sessionId}/reservations")
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
