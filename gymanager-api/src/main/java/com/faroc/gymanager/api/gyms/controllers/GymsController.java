package com.faroc.gymanager.api.gyms.controllers;

import an.awesome.pipelinr.Pipeline;
import com.faroc.gymanager.api.gyms.mappers.GymResponseMappers;
import com.faroc.gymanager.application.gyms.commands.addgym.AddGymCommand;
import com.faroc.gymanager.application.gyms.commands.deletegym.DeleteGymCommand;
import com.faroc.gymanager.domain.subscriptions.exceptions.MaxGymsReachedException;
import com.faroc.gymanager.gyms.requests.AddGymRequest;
import com.faroc.gymanager.gyms.requests.GymResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/subscriptions/{subscriptionId}/gyms")
public class GymsController {

    private final Pipeline pipeline;

    public GymsController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @PostMapping
    public ResponseEntity<GymResponse> createGym(
            @PathVariable UUID subscriptionId,
            @RequestBody AddGymRequest addGymRequest) {
        var command = new AddGymCommand(
                addGymRequest.name(),
                subscriptionId
        );

        var gym = command.execute(pipeline);

        return new ResponseEntity<>(GymResponseMappers.toResponse(gym), HttpStatus.CREATED);
    }

    @DeleteMapping("/{gymId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGym(@PathVariable UUID gymId, @PathVariable UUID subscriptionId) {
        var command = new DeleteGymCommand(gymId, subscriptionId);

        command.execute(pipeline);
    }

    @ExceptionHandler(MaxGymsReachedException.class)
    public ProblemDetail handleMaxGymsReachedException(MaxGymsReachedException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getDetail());
    }
}
