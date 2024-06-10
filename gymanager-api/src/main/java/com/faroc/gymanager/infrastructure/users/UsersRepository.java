package com.faroc.gymanager.infrastructure.users;

import com.faroc.gymanager.application.users.gateways.UsersGateway;
import com.faroc.gymanager.domain.users.User;
import org.jooq.DSLContext;
import org.jooq.codegen.maven.gymanager.tables.records.UsersRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static org.jooq.codegen.maven.gymanager.tables.Users.USERS;

@Repository
public class UsersRepository implements UsersGateway {

    private final DSLContext context;

    @Autowired
    public UsersRepository(DSLContext context) {
        this.context = context;
    }

    @Override
    public Optional<User> findById(UUID userId) {

        var user = context.selectFrom(USERS).where(USERS.ID.eq(userId)).fetchOne();

        if (user == null)
            return Optional.empty();

        var domainUser = User.MapFromStorage(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getAdminId(),
                user.getTrainerId(),
                user.getParticipantId()
        );

        return Optional.of(domainUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        var user = context.selectFrom(USERS).where(USERS.EMAIL.eq(email)).fetchOne();

        if (user == null)
            return Optional.empty();

        var domainUser = User.MapFromStorage(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getAdminId(),
                user.getTrainerId(),
                user.getParticipantId()
        );

        return Optional.of(domainUser);
    }

    @Override
    public boolean emailExists(String email) {
        return context.fetchExists(context.selectFrom(USERS).where(USERS.EMAIL.eq(email)));
    }

    @Override
    public int save(User user) {
        var userRecord = new UsersRecord();

        userRecord.setId(user.getId());
        userRecord.setFirstName(user.getFirstName());
        userRecord.setLastName(user.getLastName());
        userRecord.setEmail(user.getEmail());
        userRecord.setPasswordHash(user.getPasswordHash());

        return context.insertInto(USERS).set(userRecord).execute();
    }

    @Override
    public int update(User user) {
        return context.update(USERS)
                .set(USERS.FIRST_NAME, user.getFirstName())
                .set(USERS.LAST_NAME, user.getLastName())
                .set(USERS.EMAIL, user.getEmail())
                .set(USERS.ADMIN_ID, user.getAdminId())
                .set(USERS.TRAINER_ID, user.getTrainerId())
                .set(USERS.PARTICIPANT_ID, user.getParticipantId())
                .where(USERS.ID.eq(user.getId()))
                .execute();
    }
}
