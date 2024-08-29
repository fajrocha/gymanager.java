package com.faroc.gymanager.gymmanagement.application.gyms.queries.getsubscriptiongyms;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.gymmanagement.domain.gyms.Gym;

import java.util.List;
import java.util.UUID;

public record GetSubscriptionGymsQuery(UUID subscriptionId) implements Command<List<Gym>> {
}
