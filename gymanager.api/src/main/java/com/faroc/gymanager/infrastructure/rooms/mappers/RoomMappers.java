package com.faroc.gymanager.infrastructure.rooms.mappers;

import com.faroc.gymanager.domain.rooms.Room;
import com.faroc.gymanager.domain.shared.entities.schedules.Schedule;
import com.faroc.gymanager.domain.shared.valueobjects.timeslots.TimeSlot;
import com.faroc.gymanager.infrastructure.shared.serialization.DefaultSerializer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jooq.JSONB;
import org.jooq.codegen.maven.gymanager.tables.records.RoomsRecord;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

public class RoomMappers {
    public static RoomsRecord toRecord(Room room) {
        var roomRecord = new RoomsRecord();

        roomRecord.setId(room.getId());
        roomRecord.setGymId(room.getGymId());
        roomRecord.setName(room.getName());
        roomRecord.setMaxDailySessions(room.getMaxDailySessions());

        var calendar = DefaultSerializer.serializeTimed(room.getSchedule().getCalendar());
        roomRecord.setScheduleCalendar(JSONB.valueOf(calendar));

        var sessionsIds = DefaultSerializer.serializeTimed(room.getSessionsIds());
        roomRecord.setSessionIdsByDate(JSONB.valueOf(sessionsIds));

        roomRecord.setScheduleId(room.getSchedule().getId());

        return roomRecord;
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

        var scheduleFromRecord = jsonbToMap(roomRecord.getScheduleCalendar());
        room.getSchedule().getCalendar().putAll(scheduleFromRecord);

        return room;
    }

    private static Map<LocalDate, Set<TimeSlot>> jsonbToMap(JSONB jsonbObject) {
        var jsonMapper = new ObjectMapper();
        var jsonbObjectString = jsonbObject.toString();

        try {
            return jsonMapper.readValue(jsonbObjectString, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
