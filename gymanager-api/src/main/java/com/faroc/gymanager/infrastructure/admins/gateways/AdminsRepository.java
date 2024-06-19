package com.faroc.gymanager.infrastructure.admins.gateways;

import com.faroc.gymanager.application.admins.gateways.AdminsGateway;
import com.faroc.gymanager.domain.admins.Admin;
import org.jooq.DSLContext;
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
    public void save(Admin admin) {
         context.insertInto(ADMINS, ADMINS.ID, ADMINS.USER_ID, ADMINS.SUBSCRIPTION_ID)
                .values(admin.getId(), admin.getUserId(), admin.getSubscriptionId())
                .execute();
    }

    @Override
    public void update(Admin admin) {
        context.update(ADMINS)
                .set(ADMINS.USER_ID, admin.getUserId())
                .set(ADMINS.SUBSCRIPTION_ID, admin.getSubscriptionId())
                .execute();
    }

    @Override
    public Optional<Admin> findById(UUID id) {
        var admin = context.selectFrom(ADMINS).where(ADMINS.ID.eq(id)).fetchOne();

        if (admin == null)
            return Optional.empty();

        var adminDomain = Admin.mapFromStorage(admin.getId(), admin.getUserId(), admin.getSubscriptionId());

        return Optional.of(adminDomain);
    }
}
