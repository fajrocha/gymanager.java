package com.faroc.gymanager.infrastructure.rooms.gateways;

import com.faroc.gymanager.application.rooms.gateways.RoomsGateway;
import com.faroc.gymanager.domain.rooms.Room;
import com.faroc.gymanager.infrastructure.rooms.mappers.RoomMappers;
import org.jooq.DSLContext;
import org.jooq.codegen.maven.gymanager.Tables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
        var roomRecord = RoomMappers.toRecord(room);

        context.insertInto(ROOMS).set(roomRecord).execute();
    }
}
