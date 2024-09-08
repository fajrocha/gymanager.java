package com.faroc.gymanager.gymmanagement.application.gyms.commands.addsessioncategory;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.common.application.exceptions.ResourceNotFoundException;
import com.faroc.gymanager.gymmanagement.application.gyms.gateways.GymsGateway;
import com.faroc.gymanager.gymmanagement.domain.gyms.errors.GymsErrors;
import com.faroc.gymanager.common.domain.exceptions.UnexpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AddSessionCategoriesHandler implements Command.Handler<AddSessionCategoriesCommand, List<String>> {
    private final GymsGateway gymsGateway;

    @Autowired
    public AddSessionCategoriesHandler(GymsGateway gymsGateway) {
        this.gymsGateway = gymsGateway;
    }

    @Override
    public List<String> handle(AddSessionCategoriesCommand command) {
        var gymId = command.gymId();
        var subscriptionId = command.subscriptionId();

        var gym = gymsGateway.findById(gymId).orElseThrow(() -> new ResourceNotFoundException(
                GymsErrors.notFound(gymId, subscriptionId),
                GymsErrors.NOT_FOUND
        ));

        if (gym.getSubscriptionId() != subscriptionId)
            throw new UnexpectedException(
                    GymsErrors.subscriptionMismatch(gymId, subscriptionId, gym.getSubscriptionId()),
                    GymsErrors.SUBSCRIPTION_MISMATCH);

        var sessionCategories = command.sessionCategories();
        sessionCategories.forEach(gym::addCategory);

        gymsGateway.update(gym);

        return sessionCategories;
    }
}
