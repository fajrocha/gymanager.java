package com.faroc.gymanager.infrastructure.trainers.mappers;

import com.faroc.gymanager.domain.shared.entities.schedules.Schedule;
import com.faroc.gymanager.domain.shared.valueobjects.timeslots.TimeSlot;
import com.faroc.gymanager.domain.trainers.Trainer;
import com.faroc.gymanager.infrastructure.shared.serialization.DefaultSerializer;
import com.fasterxml.jackson.core.type.TypeReference;
import org.jooq.JSONB;
import org.jooq.codegen.maven.gymanager.tables.records.TrainersRecord;

import java.time.LocalDate;
import java.util.*;

public class TrainerMappers {
    public static TrainersRecord toRecordCreate(Trainer trainer) {
        var trainerRecord = new TrainersRecord();

        trainerRecord.setId(trainer.getId());
        mapNonIdProperties(trainerRecord, trainer);

        return trainerRecord;
    }

    public static TrainersRecord toRecordUpdate(Trainer trainer) {
        var trainerRecord = new TrainersRecord();

        mapNonIdProperties(trainerRecord, trainer);

        return trainerRecord;
    }

    public static Trainer toDomain(TrainersRecord trainerRecord) {
        var schedule = new Schedule(trainerRecord.getScheduleId());

        var trainer = new Trainer(
                trainerRecord.getId(),
                trainerRecord.getUserId(),
                schedule
        );

        var calendarTypeRef = new TypeReference<HashMap<LocalDate, Set<TimeSlot>>>() {};
        var calendarRecord = DefaultSerializer.toTimedObject(
                trainerRecord.getScheduleCalendar(),
                calendarTypeRef);
        trainer.getSchedule().mapCalendarFrom(calendarRecord);

        Arrays.stream(trainerRecord.getSessionIds()).forEach(trainer::mapSessionId);

        return trainer;
    }

    private static void mapNonIdProperties(TrainersRecord trainerRecord, Trainer trainer) {
        trainerRecord.setUserId(trainer.getUserId());
        trainerRecord.setSessionIds(trainer.getSessionsIds().toArray(new UUID[0]));
        trainerRecord.setScheduleId(trainer.getSchedule().getId());

        var serializedSchedule = DefaultSerializer.serializeTimed(trainer.getSchedule().getCalendar());
        trainerRecord.setScheduleCalendar(JSONB.valueOf(serializedSchedule));
    }
}
