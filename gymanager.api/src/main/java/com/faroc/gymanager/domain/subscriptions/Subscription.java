package com.faroc.gymanager.domain.subscriptions;

import com.faroc.gymanager.domain.shared.AggregateRoot;
import com.faroc.gymanager.domain.shared.exceptions.ConflictException;
import com.faroc.gymanager.domain.subscriptions.errors.SubscriptionErrors;
import com.faroc.gymanager.domain.subscriptions.exceptions.MaxGymsReachedException;
import lombok.Getter;

import java.util.*;

@Getter
public class Subscription extends AggregateRoot {
    public static final int MAX_GYMS_FREE = 1;
    public static final int MAX_GYMS_STARTER = 3;
    public static final int MAX_GYMS_PRO = Integer.MAX_VALUE;

    private final UUID adminId;
    private final SubscriptionType subscriptionType;
    private final int maxGyms;
    private final Set<UUID> gymIds = new HashSet<>();

    public Subscription(UUID adminId, SubscriptionType subscriptionType) {
        this.adminId = adminId;
        this.subscriptionType = subscriptionType;
        maxGyms = maxGymsFromSubType();
    }

    public void addGym(UUID gymId) {
        if (gymIds.contains(gymId))
            throw new ConflictException(
                    SubscriptionErrors.conflictGym(gymId, id),
                    SubscriptionErrors.CONFLICT_GYM);

        if (gymIds.size() == maxGyms)
            throw new MaxGymsReachedException(SubscriptionErrors.maxGymsReached(id));

        gymIds.add(gymId);
    }

    private Subscription(UUID id, UUID adminId, SubscriptionType subscriptionType, int maxGyms) {
        super(id);
        this.adminId = adminId;
        this.subscriptionType = subscriptionType;
        this.maxGyms = maxGyms;
    }

    public static Subscription mapFromStorage(
            UUID id,
            UUID adminId,
            SubscriptionType subscriptionType,
            int maxGyms,
            UUID[] gymIds) {
        var subscription = new Subscription(id, adminId, subscriptionType, maxGyms);
        Arrays.stream(gymIds).forEach(gymId -> subscription.getGymIds().add(gymId));

        return subscription;
    }

    public void removeGym(UUID gymId) {
        gymIds.remove(gymId);
    }

    public boolean hasGym(UUID gymId) {
        return gymIds.contains(gymId);
    }

    private int maxGymsFromSubType() {
        return switch (subscriptionType) {
            case Free -> MAX_GYMS_FREE;
            case Starter -> MAX_GYMS_STARTER;
            case Pro -> MAX_GYMS_PRO;
        };
    }
}
