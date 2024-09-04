package com.faroc.gymanager.gymmanagement.api.gyms.controllers.v1;

import an.awesome.pipelinr.Pipeline;
import com.faroc.gymanager.gymmanagement.api.gyms.mappers.GymResponseMappers;
import com.faroc.gymanager.gymmanagement.application.gyms.commands.addgym.AddGymCommand;
import com.faroc.gymanager.gymmanagement.application.gyms.commands.deletegym.DeleteGymCommand;
import com.faroc.gymanager.gymmanagement.application.gyms.queries.getsubscriptiongyms.GetSubscriptionGymsQuery;
import com.faroc.gymanager.sessionmanagement.application.sessions.commands.addsessioncategory.AddSessionCategoriesCommand;
import com.faroc.gymanager.gymmanagement.domain.subscriptions.exceptions.MaxGymsReachedException;
import com.faroc.gymanager.gymmanagement.api.gyms.contracts.v1.requests.AddGymRequest;
import com.faroc.gymanager.gymmanagement.api.gyms.contracts.v1.responses.GymResponse;
import com.faroc.gymanager.sessionmanagement.api.sessions.contracts.v1.requests.AddSessionCategoryRequest;
import com.faroc.gymanager.sessionmanagement.api.sessions.contracts.v1.responses.AddSessionCategoryResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController("Gyms Controller V1")
@RequestMapping("v1/subscriptions/{subscriptionId}/gyms")
@Tag(name = "Gyms")
public class GymsController {
    private final Pipeline pipeline;

    public GymsController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @GetMapping
    public List<GymResponse> getSubscriptionGyms(
            @PathVariable UUID subscriptionId) {
        var command = new GetSubscriptionGymsQuery(subscriptionId);
        var gyms = command.execute(pipeline);

        return GymResponseMappers.toResponse(gyms);
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

    @PostMapping("/{gymId}/session-categories")
    public ResponseEntity<AddSessionCategoryResponse> addGymCategories(
            @PathVariable UUID gymId,
            @RequestBody AddSessionCategoryRequest addSessionCategoryRequest) {
        var command = new AddSessionCategoriesCommand(
                gymId,
                addSessionCategoryRequest.sessionCategories());

        var sessionCategories = command.execute(pipeline);

        return new ResponseEntity<>(new AddSessionCategoryResponse(sessionCategories), HttpStatus.CREATED);
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
