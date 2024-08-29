package com.faroc.gymanager.sessionmanagement.infrastructure.participants.mappers;

import com.faroc.gymanager.sessionmanagement.domain.participants.Participant;
import com.faroc.gymanager.sessionmanagement.domain.common.schedules.Schedule;
import com.faroc.gymanager.sessionmanagement.domain.common.timeslots.TimeSlot;
import com.faroc.gymanager.common.infrastructure.serialization.DefaultSerializer;
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
