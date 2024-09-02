package com.faroc.gymanager.gymmanagement.api.subscriptions.controllers.v1;

import an.awesome.pipelinr.Pipeline;
import com.faroc.gymanager.gymmanagement.api.subscriptions.mappers.SubscriptionRequestMappers;
import com.faroc.gymanager.gymmanagement.api.subscriptions.mappers.SubscriptionResponseMappers;
import com.faroc.gymanager.gymmanagement.application.subscriptions.commands.deletesubscription.DeleteSubscriptionCommand;
import com.faroc.gymanager.gymmanagement.subscriptions.requests.SubscribeRequest;
import com.faroc.gymanager.gymmanagement.subscriptions.responses.SubscriptionResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController("Subscriptions Controller V1")
@RequestMapping("v1/subscriptions")
@Tag(name = "Subscriptions")
public class SubscriptionController {
    private final Pipeline pipeline;

    @Autowired
    public SubscriptionController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @PostMapping
    public ResponseEntity<SubscriptionResponse> createSubscription(@RequestBody SubscribeRequest createSubscriptionRequest) {
        var command = SubscriptionRequestMappers.toCommand(createSubscriptionRequest);

        var subscription = command.execute(pipeline);

        return new ResponseEntity<>(SubscriptionResponseMappers.toResponse(subscription), HttpStatus.CREATED);
    }

    @DeleteMapping("{subscriptionId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void createSubscription(@PathVariable UUID subscriptionId) {
        var command = new DeleteSubscriptionCommand(subscriptionId);

        command.execute(pipeline);
    }
}
