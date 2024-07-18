package com.faroc.gymanager.infrastructure.users.gateways;

import com.faroc.gymanager.application.users.gateways.TrainersGateway;
import com.faroc.gymanager.domain.trainers.Trainer;
import com.faroc.gymanager.infrastructure.shared.serialization.DefaultSerializer;
import com.faroc.gymanager.infrastructure.users.mappers.TrainerMappers;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;


import static org.jooq.codegen.maven.gymanager.Tables.TRAINERS;

@Repository
public class TrainersRepository implements TrainersGateway {
    private final DSLContext context;

    public TrainersRepository(DSLContext context) {
        this.context = context;
    }

    @Override
    public void create(Trainer trainer) {
        var trainerRecord = TrainerMappers.toRecord(trainer);

        context.insertInto(TRAINERS).set(trainerRecord).execute();
    }
}
