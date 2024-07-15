package com.faroc.gymanager.infrastructure.rooms.mappers;

import com.faroc.gymanager.domain.rooms.Room;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jooq.JSONB;
import org.jooq.codegen.maven.gymanager.tables.records.RoomsRecord;

public class RoomMappers {
    public static RoomsRecord toRecord(Room room) {
        var roomRecord = new RoomsRecord();

        roomRecord.setId(room.getId());
        roomRecord.setGymId(room.getGymId());
        roomRecord.setName(room.getName());
        roomRecord.setMaxDailySessions(room.getMaxDailySessions());

        var calendar = serializeTimed(room.getSchedule().getCalendar());
        roomRecord.setScheduleCalendar(JSONB.valueOf(calendar));

        var sessionsIds = serializeTimed(room.getSessionsIds());
        roomRecord.setSessionIdsByDate(JSONB.valueOf(sessionsIds));

        roomRecord.setScheduleId(room.getSchedule().getId());

        return roomRecord;
    }

    private static String serializeTimed(Object content) {
        var jsonMapper = new ObjectMapper();
        jsonMapper.registerModule(new JavaTimeModule());
        jsonMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            return jsonMapper.writeValueAsString(content);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
