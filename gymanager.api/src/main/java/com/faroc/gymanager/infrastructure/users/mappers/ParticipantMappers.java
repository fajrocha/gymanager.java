package com.faroc.gymanager.infrastructure.users.mappers;

import com.faroc.gymanager.domain.participants.Participant;
import com.faroc.gymanager.domain.shared.entities.schedules.Schedule;
import com.faroc.gymanager.domain.shared.valueobjects.timeslots.TimeSlot;
import com.faroc.gymanager.infrastructure.shared.serialization.DefaultSerializer;
import com.fasterxml.jackson.core.type.TypeReference;
import org.jooq.JSONB;
import org.jooq.codegen.maven.gymanager.tables.records.ParticipantsRecord;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class ParticipantMappers {

    public static ParticipantsRecord toRecordCreate(Participant participant) {
        var participantsRecord = new ParticipantsRecord();

        participantsRecord.setId(participant.getId());
        mapNonIdProperties(participantsRecord, participant);

        return participantsRecord;
    }

    public static ParticipantsRecord toRecordUpdate(Participant participant) {
        var participantsRecord = new ParticipantsRecord();

        mapNonIdProperties(participantsRecord, participant);

        return participantsRecord;
    }

    public static Participant toDomain(ParticipantsRecord participantsRecord) {
        var schedule = new Schedule(participantsRecord.getScheduleId());

        var participant = new Participant(
                participantsRecord.getId(),
                participantsRecord.getUserId(),
                schedule
        );

        var calendarTypeRef = new TypeReference<HashMap<LocalDate, Set<TimeSlot>>>() {};
        var calendarRecord = DefaultSerializer.toTimedObject(
                participantsRecord.getScheduleCalendar(),
                calendarTypeRef);
        participant.getSchedule().mapCalendarFrom(calendarRecord);

        Arrays.stream(participantsRecord.getSessionIds()).forEach(participant::mapSessionId);

        return participant;
    }

    private static void mapNonIdProperties(ParticipantsRecord participantRecord, Participant participant) {
        participantRecord.setUserId(participant.getUserId());
        participantRecord.setSessionIds(participant.getSessionsIds().toArray(new UUID[0]));
        participantRecord.setScheduleId(participant.getSchedule().getId());

        var serializedSchedule = DefaultSerializer.serializeTimed(participant.getSchedule().getCalendar());
        participantRecord.setScheduleCalendar(JSONB.valueOf(serializedSchedule));
    }
}
