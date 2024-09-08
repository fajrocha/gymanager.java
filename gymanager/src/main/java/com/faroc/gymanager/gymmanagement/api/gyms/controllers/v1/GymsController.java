package com.faroc.gymanager.gymmanagement.api.gyms.controllers.v1;

import an.awesome.pipelinr.Pipeline;
import com.faroc.gymanager.gymmanagement.api.gyms.mappers.GymResponseMappers;
import com.faroc.gymanager.gymmanagement.application.gyms.commands.addgym.AddGymCommand;
import com.faroc.gymanager.gymmanagement.application.gyms.commands.deletegym.DeleteGymCommand;
import com.faroc.gymanager.gymmanagement.application.gyms.queries.getsubscriptiongyms.GetSubscriptionGymsQuery;
import com.faroc.gymanager.gymmanagement.application.gyms.commands.addsessioncategory.AddSessionCategoriesCommand;
import com.faroc.gymanager.gymmanagement.domain.subscriptions.exceptions.MaxGymsReachedException;
import com.faroc.gymanager.gymmanagement.api.gyms.contracts.v1.requests.AddGymRequest;
import com.faroc.gymanager.gymmanagement.api.gyms.contracts.v1.responses.GymResponse;
import com.faroc.gymanager.sessionmanagement.api.sessions.contracts.v1.requests.AddSessionCategoryRequest;
import com.faroc.gymanager.sessionmanagement.api.sessions.contracts.v1.responses.AddSessionCategoryResponse;
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

import java.util.List;
import java.util.UUID;

@RestController("Gyms Controller V1")
@RequestMapping("v1/subscriptions/{subscriptionId}/gyms")
@Tag(name = "Gyms", description = "Requests to manage gyms.")
public class GymsController {
    private final Pipeline pipeline;

    public GymsController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @GetMapping
    @Operation(
            summary = "Fetch gyms from a subscription.",
            description = "Fetches the gyms associated to a given subscription."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Gyms associated to a given subscription.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GymResponse[].class)
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
                    description = "Subscription not found.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error during request to subscribe.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    })
    })
    public List<GymResponse> getSubscriptionGyms(
            @Parameter(description = "Subscription id attached to gyms to fetch.") @PathVariable UUID subscriptionId) {
        var command = new GetSubscriptionGymsQuery(subscriptionId);
        var gyms = command.execute(pipeline);

        return GymResponseMappers.toResponse(gyms);
    }

    @PostMapping
    @Operation(
            summary = "Adds a gym to a subscription.",
            description = "Adds a gym to a given subscription."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Gym added.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GymResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "User is not authenticated.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "User does not have permissions to add gyms.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error when adding the gym, for example due to " +
                            "subscription not existing.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    })
    })
    public ResponseEntity<GymResponse> createGym(
            @Parameter(description = "Id of subscription associated to gym to add.") @PathVariable UUID subscriptionId,
            @RequestBody AddGymRequest addGymRequest) {
        var command = new AddGymCommand(
                addGymRequest.name(),
                subscriptionId
        );

        var gym = command.execute(pipeline);

        return new ResponseEntity<>(GymResponseMappers.toResponse(gym), HttpStatus.CREATED);
    }

    @PostMapping("/{gymId}/session-categories")
    @Operation(
            summary = "Adds session categories to a gym.",
            description = "Adds further session categories to a given gym."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Session categories added tog gym.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AddSessionCategoryResponse.class)
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
                    description = "Subscription not found.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error when adding the gym, for example due to " +
                            "subscription given not matching the subscription associated to gym.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    })
    })
    public AddSessionCategoryResponse addGymCategories(
            @Parameter(description = "Id of gym to add session categories.") @PathVariable UUID gymId,
            @Parameter(description = "Id of subscription associated to gym.") @PathVariable UUID subscriptionId,
            @Valid @RequestBody AddSessionCategoryRequest addSessionCategoryRequest) {
        var command = new AddSessionCategoriesCommand(
                gymId,
                subscriptionId,
                addSessionCategoryRequest.sessionCategories());

        var sessionCategories = command.execute(pipeline);

        return new AddSessionCategoryResponse(sessionCategories);
    }

    @DeleteMapping("/{gymId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Deletes a gym from subscription.",
            description = "Deletes a gym from a given subscription."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Gym deleted.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "User is not authenticated.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "User does not have permissions to delete gyms.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Subscription not found.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error when deleting the gym, for example due to " +
                            "subscription not having the gym.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    })
    })
    public void deleteGym(
            @Parameter(description = "Id of gym to delete.") @PathVariable UUID gymId,
            @Parameter(description = "Id of subscription associated to gym.") @PathVariable UUID subscriptionId) {
        var command = new DeleteGymCommand(gymId, subscriptionId);

        command.execute(pipeline);
    }

    @ExceptionHandler(MaxGymsReachedException.class)
    public ProblemDetail handleMaxGymsReachedException(MaxGymsReachedException ex) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getDetail());
    }
}
