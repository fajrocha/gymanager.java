package com.faroc.gymanager.infrastructure.users.mappers;

import com.faroc.gymanager.domain.users.User;
import org.jooq.codegen.maven.gymanager.tables.records.UsersRecord;

public class UsersMappers {
    public static UsersRecord toRecordCreate(User user) {
        var record = new UsersRecord();

        record.setId(user.getId());
        record.setFirstName(user.getFirstName());
        record.setLastName(user.getLastName());
        record.setEmail(user.getEmail());
        record.setPasswordHash(user.getPasswordHash());

        return record;
    }

    public static UsersRecord toRecordUpdate(User user) {
        var record = new UsersRecord();

        record.setFirstName(user.getFirstName());
        record.setLastName(user.getLastName());
        record.setEmail(user.getEmail());
        record.setPasswordHash(user.getPasswordHash());
        record.setAdminId(user.getAdminId());
        record.setTrainerId(user.getTrainerId());
        record.setParticipantId(user.getParticipantId());

        return record;
    }

    public static User toDomain(UsersRecord record) {
        var user = new User(
                record.getId(),
                record.getFirstName(),
                record.getLastName(),
                record.getEmail(),
                record.getPasswordHash()
        );

        user.mapAdminId(record.getAdminId())
                .mapTrainerId(record.getTrainerId())
                .mapParticipantId(record.getParticipantId());

        return user;
    }
}
