 package com.faroc.gymanager.gymmanagement.domain.subscriptions;

import com.faroc.gymanager.common.domain.AggregateRoot;
import com.faroc.gymanager.common.domain.exceptions.ConflictException;
import com.faroc.gymanager.gymmanagement.domain.gyms.Gym;
import com.faroc.gymanager.gymmanagement.domain.subscriptions.errors.SubscriptionErrors;
import com.faroc.gymanager.gymmanagement.domain.subscriptions.events.AddGymEvent;
import com.faroc.gymanager.gymmanagement.domain.subscriptions.events.RemoveGymEvent;
import com.faroc.gymanager.gymmanagement.domain.subscriptions.exceptions.MaxGymsReachedException;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.*;

public class Subscription extends AggregateRoot {
    public static final int MAX_GYMS_FREE = 1;
    public static final int MAX_GYMS_STARTER = 3;
    public static final int MAX_GYMS_PRO = Integer.MAX_VALUE;

    public static final int MAX_ROOMS_FREE = 1;
    public static final int MAX_ROOMS_STARTER = 10;
    public static final int MAX_ROOMS_PRO = Integer.MAX_VALUE;

    public static final int MAX_SESSIONS_FREE = 3;
    public static final int MAX_SESSIONS_STARTER = Integer.MAX_VALUE;
    public static final int MAX_SESSIONS_PRO = Integer.MAX_VALUE;

    @Getter
    private final UUID adminId;
    @Getter
    private final SubscriptionType subscriptionType;
    private final int maxGyms;
    private final Set<UUID> gymIds = new HashSet<>();

    public Subscription(UUID adminId, SubscriptionType subscriptionType) {
        this.adminId = adminId;
        this.subscriptionType = subscriptionType;
        maxGyms = getMaxGyms();
    }

    public Subscription(UUID id, UUID adminId, SubscriptionType subscriptionType) {
        super(id);
        this.adminId = adminId;
        this.subscriptionType = subscriptionType;
        maxGyms = getMaxGyms();
    }

    private Subscription(
            @JsonProperty("id") UUID id,
            @JsonProperty("adminId") UUID adminId,
            @JsonProperty("subscriptionType") SubscriptionType subscriptionType,
            @JsonProperty("maxGyms") int maxGyms) {
        super(id);
        this.adminId = adminId;
        this.subscriptionType = subscriptionType;
        this.maxGyms = maxGyms;
    }

    public static Subscription map(UUID id, UUID adminId, SubscriptionType subscriptionType, int maxGyms) {
        return new Subscription(id, adminId, subscriptionType, maxGyms);
    }

    public void addGym(Gym gym) {
        addGym(gym.getId());

        domainEvents.add(new AddGymEvent(this, gym));
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

    public void removeGym(Gym gym) {
        gymIds.remove(gym.getId());

        domainEvents.add(new RemoveGymEvent(this, gym));
    }

    public void removeGym(UUID gymId) {
        gymIds.remove(gymId);
    }

    public boolean hasGym(UUID gymId) {
        return gymIds.contains(gymId);
    }

    public Set<UUID> getGymIds() {
        return Collections.unmodifiableSet(gymIds);
    }

    public int getMaxGyms() {
        return getMaxGyms(subscriptionType);
    }

    public static int getMaxGyms(SubscriptionType subscriptionType) {
        return switch (subscriptionType) {
            case Free -> MAX_GYMS_FREE;
            case Starter -> MAX_GYMS_STARTER;
            case Pro -> MAX_GYMS_PRO;
        };
    }

    public int getMaxRooms() {
        return switch (subscriptionType) {
            case Free -> MAX_ROOMS_FREE;
            case Starter -> MAX_ROOMS_STARTER;
            case Pro -> MAX_ROOMS_PRO;
        };
    }

    public int getMaxDailySessions() {
        return switch (subscriptionType) {
            case Free -> MAX_SESSIONS_FREE;
            case Starter -> MAX_SESSIONS_STARTER;
            case Pro -> MAX_SESSIONS_PRO;
        };
    }
}
