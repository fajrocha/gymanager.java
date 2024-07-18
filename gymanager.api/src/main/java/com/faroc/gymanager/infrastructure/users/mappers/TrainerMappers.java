package com.faroc.gymanager.infrastructure.users.mappers;

import com.faroc.gymanager.domain.trainers.Trainer;
import com.faroc.gymanager.infrastructure.shared.serialization.DefaultSerializer;
import org.jooq.JSONB;
import org.jooq.codegen.maven.gymanager.tables.records.TrainersRecord;

import java.util.UUID;

public class TrainerMappers {
    public static TrainersRecord toRecord(Trainer trainer) {
        var trainerRecord = new TrainersRecord();

        trainerRecord.setId(trainer.getId());
        trainerRecord.setUserId(trainer.getUserId());
        trainerRecord.setSessionIds(trainer.getSessionsIds().toArray(new UUID[0]));
        trainerRecord.setScheduleId(trainer.getSchedule().getId());

        var serializedSchedule = DefaultSerializer.serializeTimed(trainer.getSchedule().getCalendar());
        trainerRecord.setScheduleCalendar(JSONB.valueOf(serializedSchedule));

        return trainerRecord;
    }
}
