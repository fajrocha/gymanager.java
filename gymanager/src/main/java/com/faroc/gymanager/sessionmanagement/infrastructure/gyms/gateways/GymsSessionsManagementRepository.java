package com.faroc.gymanager.sessionmanagement.infrastructure.gyms.gateways;

import com.faroc.gymanager.sessionmanagement.application.gyms.gateways.GymsGateway;
import com.faroc.gymanager.sessionmanagement.domain.gyms.Gym;
import com.faroc.gymanager.sessionmanagement.infrastructure.gyms.mappers.GymMappers;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static org.jooq.codegen.maven.gymanager.Tables.GYMS;

@Repository
public class GymsSessionsManagementRepository implements GymsGateway {
    private final DSLContext context;

    @Autowired
    public GymsSessionsManagementRepository(DSLContext context) {
        this.context = context;
    }

    @Override
    public Optional<Gym> findById(UUID id) {
        var gymRecord = context.selectFrom(GYMS).where(GYMS.ID.eq(id)).fetchOne();

        if (gymRecord == null)
            return Optional.empty();

        var gym = GymMappers.toDomain(gymRecord);

        return Optional.of(gym);
    }

    @Override
    public void updateSessions(Gym gym) {
        context.update(GYMS)
                .set(GYMS.SESSION_CATEGORIES, gym.getSessionCategories().toArray(new String[0]))
                .where(GYMS.ID.eq(gym.getId()))
                .execute();
    }
}
