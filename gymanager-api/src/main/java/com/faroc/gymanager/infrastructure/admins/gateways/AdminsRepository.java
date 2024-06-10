package com.faroc.gymanager.infrastructure.admins.gateways;

import com.faroc.gymanager.application.admins.gateways.AdminsGateway;
import com.faroc.gymanager.domain.admins.Admin;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static org.jooq.codegen.maven.gymanager.Tables.ADMINS;

@Repository
public class AdminsRepository implements AdminsGateway {

    private final DSLContext context;

    @Autowired
    public AdminsRepository(DSLContext context) {
        this.context = context;
    }

    @Override
    public int save(Admin admin) {
         return context
                .insertInto(ADMINS, ADMINS.ID, ADMINS.USER_ID, ADMINS.SUBSCRIPTION_ID)
                .values(admin.getId(), admin.getUserId(), admin.getSubscriptionId())
                .execute();
    }
}
