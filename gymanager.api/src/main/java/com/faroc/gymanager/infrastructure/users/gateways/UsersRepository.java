package com.faroc.gymanager.infrastructure.users.gateways;

import com.faroc.gymanager.application.users.gateways.UsersGateway;
import com.faroc.gymanager.domain.users.User;
import com.faroc.gymanager.infrastructure.users.mappers.UsersMappers;
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
        var record = context.selectFrom(USERS).where(USERS.ID.eq(userId)).fetchOne();

        if (record == null)
            return Optional.empty();

        var user = UsersMappers.toDomain(record);

        return Optional.of(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        var record = context.selectFrom(USERS).where(USERS.EMAIL.eq(email)).fetchOne();

        if (record == null)
            return Optional.empty();

        var user = UsersMappers.toDomain(record);

        return Optional.of(user);
    }

    @Override
    public boolean emailExists(String email) {
        return context.fetchExists(context.selectFrom(USERS).where(USERS.EMAIL.eq(email)));
    }

    @Override
    public void create(User user) {
        var record = UsersMappers.toRecordCreate(user);

        context.insertInto(USERS).set(record).execute();
    }

    @Override
    public void update(User user) {
        var record = UsersMappers.toRecordUpdate(user);

        context.update(USERS)
                .set(record)
                .where(USERS.ID.eq(user.getId()))
                .execute();
    }

    @Override
    public void delete(UUID id) {
        context.deleteFrom(USERS).where(USERS.ID.eq(id)).execute();
    }
}
