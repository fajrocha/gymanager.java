package com.faroc.gymanager.gymmanagement.application.gyms.queries.getsubscriptiongyms;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.gymmanagement.application.gyms.gateways.GymsGateway;
import com.faroc.gymanager.gymmanagement.domain.gyms.Gym;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetSubscriptionGymsHandler implements Command.Handler<GetSubscriptionGymsQuery, List<Gym>> {
    private final GymsGateway gymsGateway;

    public GetSubscriptionGymsHandler(GymsGateway gymsGateway) {
        this.gymsGateway = gymsGateway;
    }

    @Override
    public List<Gym> handle(GetSubscriptionGymsQuery getSubscriptionGymsQuery) {
        return gymsGateway.findBySubscriptionId(getSubscriptionGymsQuery.subscriptionId());
    }
}
