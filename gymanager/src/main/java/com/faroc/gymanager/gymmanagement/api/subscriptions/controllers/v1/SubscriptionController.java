package com.faroc.gymanager.gymmanagement.api.subscriptions.controllers.v1;

import an.awesome.pipelinr.Pipeline;
import com.faroc.gymanager.gymmanagement.api.subscriptions.mappers.SubscriptionRequestMappers;
import com.faroc.gymanager.gymmanagement.api.subscriptions.mappers.SubscriptionResponseMappers;
import com.faroc.gymanager.gymmanagement.application.subscriptions.commands.unsubscribe.UnsubscribeCommand;
import com.faroc.gymanager.gymmanagement.api.subscriptions.contracts.v1.requests.SubscribeRequest;
import com.faroc.gymanager.gymmanagement.api.subscriptions.contracts.v1.responses.SubscriptionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController("Subscriptions Controller V1")
@RequestMapping("v1/subscriptions")
@Tag(name = "Subscriptions", description = "Requests to manage the admins subscriptions.")
public class SubscriptionController {
    private final Pipeline pipeline;

    @Autowired
    public SubscriptionController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @PostMapping
    @Operation(
            summary = "Subscribe as an admin user.",
            description = "Subscription creation on the platform by an admin user."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Admin subscribed, response with subscription id.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SubscriptionResponse.class)
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
                    description = "Resource not found, for example when the admin does not exist.",
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
    public ResponseEntity<SubscriptionResponse> subscribe(
            @Valid @RequestBody SubscribeRequest subscribeRequest) {
        var command = SubscriptionRequestMappers.toCommand(subscribeRequest);

        var subscription = command.execute(pipeline);

        return new ResponseEntity<>(SubscriptionResponseMappers.toResponse(subscription), HttpStatus.CREATED);
    }

    @DeleteMapping("{subscriptionId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Unsubscribe as an admin user.",
            description = "Subscription deletion on the platform by an admin user."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Admin unsubscribed, no content response.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "User is not authenticated.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Resource not found, for example when the admin does not exist.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error during request to unsubscribe.",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ProblemDetail.class)
                            )
                    })
    })
    public void unsubscribe(
            @Parameter(description = "Subscription id to unsubscribe.")
            @PathVariable UUID subscriptionId) {
        var command = new UnsubscribeCommand(subscriptionId);

        command.execute(pipeline);
    }
}
