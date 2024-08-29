package com.faroc.gymanager.gymmanagement.infrastructure.admins.gateway;

import com.faroc.gymanager.gymmanagement.application.admins.gateways.AdminsGateway;
import com.faroc.gymanager.gymmanagement.domain.admins.Admin;
import org.jooq.DSLContext;
import org.jooq.codegen.maven.gymanager.tables.records.AdminsRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static org.jooq.codegen.maven.gymanager.Tables.ADMINS;

@Repository
public class AdminsRepository implements AdminsGateway {

    private final DSLContext context;

    @Autowired
    public AdminsRepository(DSLContext context) {
        this.context = context;
    }

    @Override
    public void create(Admin admin) {
        var record = new AdminsRecord();

        record.setId(admin.getId());
        record.setUserId(admin.getUserId());
        record.setSubscriptionId(admin.getSubscriptionId());

        context.insertInto(ADMINS)
                .set(record)
                .execute();
    }

    @Override
    public void update(Admin admin) {
        context.update(ADMINS)
                .set(ADMINS.USER_ID, admin.getUserId())
                .set(ADMINS.SUBSCRIPTION_ID, admin.getSubscriptionId())
                .where(ADMINS.ID.eq(admin.getId()))
                .execute();
    }

    @Override
    public Optional<Admin> findById(UUID id) {
        var admin = context.selectFrom(ADMINS).where(ADMINS.ID.eq(id)).fetchOne();

        if (admin == null)
            return Optional.empty();

        var adminDomain = Admin.map(admin.getId(), admin.getUserId(), admin.getSubscriptionId());

        return Optional.of(adminDomain);
    }
}
