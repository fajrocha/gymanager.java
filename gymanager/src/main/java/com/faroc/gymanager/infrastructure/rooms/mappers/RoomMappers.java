package com.faroc.gymanager.infrastructure.rooms.mappers;

import com.faroc.gymanager.domain.rooms.Room;
import com.faroc.gymanager.domain.shared.entities.schedules.Schedule;
import com.faroc.gymanager.domain.shared.valueobjects.timeslots.TimeSlot;
import com.faroc.gymanager.infrastructure.shared.serialization.DefaultSerializer;
import com.fasterxml.jackson.core.type.TypeReference;
import org.jooq.JSONB;
import org.jooq.codegen.maven.gymanager.tables.records.RoomsRecord;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class RoomMappers {
    public static RoomsRecord toRecordCreate(Room room) {
        var roomRecord = new RoomsRecord();

        roomRecord.setId(room.getId());
        mapNonIdProperties(room, roomRecord);

        return roomRecord;
    }

    public static RoomsRecord toRecordUpdate(Room room) {
        var roomRecord = new RoomsRecord();

        mapNonIdProperties(room, roomRecord);

        return roomRecord;
    }

    private static void mapNonIdProperties(Room room, RoomsRecord roomRecord) {
        roomRecord.setGymId(room.getGymId());
        roomRecord.setName(room.getName());
        roomRecord.setMaxDailySessions(room.getMaxDailySessions());

        var calendar = DefaultSerializer.serializeTimed(room.getSchedule().getCalendar());
        roomRecord.setScheduleCalendar(JSONB.valueOf(calendar));

        var sessionsIds = DefaultSerializer.serializeTimed(room.getSessionsIds());
        roomRecord.setSessionIdsByDate(JSONB.valueOf(sessionsIds));

        roomRecord.setScheduleId(room.getSchedule().getId());
    }

    public static Room toDomain(RoomsRecord roomRecord) {
        var scheduleId = roomRecord.getScheduleId();
        var schedule = new Schedule(scheduleId);

        var room = new Room(
                roomRecord.getId(),
                roomRecord.getGymId(),
                roomRecord.getName(),
                roomRecord.getMaxDailySessions(),
                schedule
        );

        var calendarType = new TypeReference<HashMap<LocalDate, Set<TimeSlot>>>() {};
        var calendarRecord = DefaultSerializer.toTimedObject(
                roomRecord.getScheduleCalendar(),
                calendarType);

        room.getSchedule().mapCalendarFrom(calendarRecord);

        var sessionIdsType = new TypeReference<HashMap<LocalDate, Set<UUID>>>() {};
        var sessionIdsRecord = DefaultSerializer.toTimedObject(
                roomRecord.getSessionIdsByDate(),
                sessionIdsType);

        room.mapSessionIdsFrom(sessionIdsRecord);

        return room;
    }
}
