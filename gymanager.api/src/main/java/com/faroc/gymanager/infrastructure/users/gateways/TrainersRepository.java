package com.faroc.gymanager.infrastructure.users.gateways;

import com.faroc.gymanager.application.users.gateways.TrainersGateway;
import com.faroc.gymanager.domain.trainers.Trainer;
import com.faroc.gymanager.infrastructure.users.mappers.TrainerMappers;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;


import java.util.Optional;
import java.util.UUID;

import static org.jooq.codegen.maven.gymanager.Tables.TRAINERS;

@Repository
public class TrainersRepository implements TrainersGateway {
    private final DSLContext context;

    public TrainersRepository(DSLContext context) {
        this.context = context;
    }

    @Override
    public void create(Trainer trainer) {
        var trainerRecord = TrainerMappers.toRecordCreate(trainer);

        context.insertInto(TRAINERS).set(trainerRecord).execute();
    }

    @Override
    public void update(Trainer trainer) {
        var trainerRecord = TrainerMappers.toRecordUpdate(trainer);

        context.update(TRAINERS).set(trainerRecord).execute();
    }

    @Override
    public Optional<Trainer> findById(UUID id) {
        var trainerRecord = context.selectFrom(TRAINERS)
                .where(TRAINERS.ID.eq(id))
                .fetchOne();

        if (trainerRecord == null)
            return Optional.empty();

        var trainer = TrainerMappers.toDomain(trainerRecord);

        return Optional.of(trainer);
    }
}
