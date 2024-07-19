package com.faroc.gymanager.infrastructure.shared.serialization;

import com.faroc.gymanager.domain.shared.valueobjects.timeslots.TimeSlot;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jooq.JSONB;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class DefaultSerializer {
    public static String serialize(Object content) {
        var jsonMapper = new ObjectMapper();

        try {
            return jsonMapper.writeValueAsString(content);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String serializeTimed(Object content) {
        var jsonMapper = getTimedMapper();

        try {
            return jsonMapper.writeValueAsString(content);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T toTimedObject(
            JSONB jsonbObject,
            TypeReference<T> typeRef) {
        var jsonMapper = getTimedMapper();
        var jsonbObjectString = jsonbObject.toString();

        try {
            return jsonMapper.readValue(jsonbObjectString, typeRef);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<LocalDate, Set<TimeSlot>> toTimeSlotMap(JSONB jsonbObject) {
        var jsonMapper = getTimedMapper();
        var jsonbObjectString = jsonbObject.toString();

        try {
            return jsonMapper.readValue(jsonbObjectString, new TypeReference<HashMap<LocalDate, Set<TimeSlot>>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<LocalDate, Set<UUID>> toIdMap(JSONB jsonbObject) {
        var jsonMapper = getTimedMapper();
        var jsonbObjectString = jsonbObject.toString();

        try {
            return jsonMapper.readValue(jsonbObjectString, new TypeReference<HashMap<LocalDate, Set<UUID>>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static ObjectMapper getTimedMapper() {
        var jsonMapper = new ObjectMapper();
        jsonMapper.registerModule(new JavaTimeModule());
        jsonMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return jsonMapper;
    }


}
