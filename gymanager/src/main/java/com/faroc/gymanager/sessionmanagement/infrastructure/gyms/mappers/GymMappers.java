package com.faroc.gymanager.sessionmanagement.infrastructure.gyms.mappers;

import com.faroc.gymanager.sessionmanagement.domain.gyms.Gym;
import org.jooq.codegen.maven.gymanager.tables.records.GymsRecord;

import java.util.Arrays;

public class GymMappers {
    public static Gym toDomain(GymsRecord gymRecord) {
        var gym = new Gym(gymRecord.getId());

        Arrays.stream(gymRecord.getSessionCategories()).forEach(gym::addCategory);

        return gym;
    }
}
