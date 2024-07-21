package com.faroc.gymanager.infrastructure.rooms.gateways;

import com.faroc.gymanager.application.rooms.gateways.RoomsGateway;
import com.faroc.gymanager.domain.rooms.Room;
import com.faroc.gymanager.infrastructure.rooms.mappers.RoomMappers;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static org.jooq.codegen.maven.gymanager.Tables.ROOMS;

@Repository
public class RoomsRepository implements RoomsGateway {
    private final DSLContext context;

    @Autowired
    public RoomsRepository(DSLContext context) {
        this.context = context;
    }

    @Override
    public void create(Room room) {
        var roomRecord = RoomMappers.toRecordCreate(room);

        context.insertInto(ROOMS).set(roomRecord).execute();
    }

    @Override
    public void update(Room room) {
        var roomRecord = RoomMappers.toRecordUpdate(room);

        context.update(ROOMS).set(roomRecord).execute();
    }

    @Override
    public Optional<Room> findById(UUID id) {
        var roomRecord = context.selectFrom(ROOMS).where(ROOMS.ID.eq(id)).fetchOne();

        if (roomRecord == null)
            return Optional.empty();

        var room = RoomMappers.toDomain(roomRecord);

        return Optional.of(room);
    }
}
