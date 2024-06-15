package com.faroc.gymanager.domain.subscriptions;

import an.awesome.pipelinr.Notification;
import com.faroc.gymanager.domain.shared.exceptions.ConflictException;
import lombok.Getter;

import java.util.*;

public class Subscription {
    public static final int MAX_GYMS_FREE = 1;
    public static final int MAX_GYMS_STARTER = 3;
    public static final int MAX_GYMS_PRO = Integer.MAX_VALUE;

    @Getter
    private final UUID id;
    @Getter
    private final UUID adminId;
    @Getter
    private final SubscriptionType subscriptionType;
    @Getter
    private final int maxGyms;
    @Getter
    private final Set<UUID> gymIds = new HashSet<>();
    private final List<Notification> domainEvents = new ArrayList<>();

    public Subscription(UUID adminId, SubscriptionType subscriptionType) {
        this.id = UUID.randomUUID();
        this.adminId = adminId;
        this.subscriptionType = subscriptionType;
        maxGyms = maxGymsFromSubType();
    }

    public void addGym(UUID gymId) {
        if (gymIds.contains(gymId))
            throw new ConflictException(
                    SubscriptionErrors.conflictGym(gymId, id),
                    SubscriptionErrors.CONFLICT_GYM);

        gymIds.add(gymId);
    }

    public void removeGym(UUID gymId) {
        gymIds.remove(gymId);
    }

    public boolean hasGym(UUID gymId) {
        return gymIds.contains(gymId);
    }

    public void addDomainEvents(Notification event) {
        domainEvents.add(event);
    }

    public List<Notification> popEvents() {
        var domainEvents = this.domainEvents;
        this.domainEvents.clear();

        return domainEvents;
    }

    private int maxGymsFromSubType() {
        return switch (subscriptionType) {
            case Free -> MAX_GYMS_FREE;
            case Starter -> MAX_GYMS_STARTER;
            case Pro -> MAX_GYMS_PRO;
        };
    }
}
