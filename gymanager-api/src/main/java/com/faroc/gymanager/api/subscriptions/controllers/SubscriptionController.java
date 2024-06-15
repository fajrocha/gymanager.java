package com.faroc.gymanager.api.subscriptions.controllers;

import an.awesome.pipelinr.Pipeline;
import com.faroc.gymanager.api.subscriptions.mappers.SubscriptionRequestMappers;
import com.faroc.gymanager.api.subscriptions.mappers.SubscriptionResponseMappers;
import com.faroc.gymanager.subscriptions.requests.CreateSubscriptionRequest;
import com.faroc.gymanager.subscriptions.responses.SubscriptionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {
    private final Pipeline pipeline;

    @Autowired
    public SubscriptionController(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @PostMapping
    public SubscriptionResponse createSubscription(@RequestBody CreateSubscriptionRequest createSubscriptionRequest) {
        var command = SubscriptionRequestMappers.toCommand(createSubscriptionRequest);

        var subscription = command.execute(pipeline);

        return SubscriptionResponseMappers.toResponse(subscription);
    }
}
