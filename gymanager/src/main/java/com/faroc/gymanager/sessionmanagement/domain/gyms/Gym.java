package com.faroc.gymanager.sessionmanagement.domain.gyms;

import com.faroc.gymanager.common.domain.AggregateRoot;
import com.faroc.gymanager.common.domain.exceptions.ConflictException;
import com.faroc.gymanager.gymmanagement.domain.gyms.errors.GymsErrors;

import java.util.*;

public class Gym extends AggregateRoot {
    private final Set<String> sessionCategories = new HashSet<>();

    public Gym(UUID id) {
        super(id);
    }

    public void addCategory(String sessionCategory) {
        if (sessionCategories.contains(sessionCategory))
            throw new ConflictException(
                    GymsErrors.conflictSessionCategory(sessionCategory, id),
                    GymsErrors.conflictSessionCategory(sessionCategory));

        sessionCategories.add(sessionCategory);
    }

    public boolean supportsCategory(String categoryName) {
        return sessionCategories.contains(categoryName);
    }
    public Set<String> getSessionCategories() {
        return Collections.unmodifiableSet(sessionCategories);
    }
}
