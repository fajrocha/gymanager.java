package com.faroc.gymanager.sessionmanagement.infrastructure.sessions.gateways;

import com.faroc.gymanager.sessionmanagement.application.sessions.gateways.SessionCategoriesGateway;
import com.faroc.gymanager.sessionmanagement.domain.sessions.SessionCategory;
import org.jooq.DSLContext;
import org.jooq.codegen.maven.gymanager.Tables;
import org.jooq.codegen.maven.gymanager.tables.SessionCategories;
import org.jooq.codegen.maven.gymanager.tables.Sessions;
import org.jooq.codegen.maven.gymanager.tables.records.SessionCategoriesRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.jooq.codegen.maven.gymanager.Tables.SESSION_CATEGORIES;

@Repository
public class SessionCategoriesRepository implements SessionCategoriesGateway {
    private final DSLContext context;

    public SessionCategoriesRepository(DSLContext context) {
        this.context = context;
    }

    @Override
    public void createAll(List<SessionCategory> sessionCategories) {
        var categoriesRecords = sessionCategories
                .stream()
                .map(category -> {
                    var record = new SessionCategoriesRecord();

                    record.setId(category.getId());
                    record.setName(category.getName());

                    return record;
                })
                .toList();

        context.batchInsert(categoriesRecords).execute();
    }

    @Override
    public boolean existsByName(String categoryName) {
        return context.fetchExists(
                context.selectFrom(SESSION_CATEGORIES)
                        .where(SESSION_CATEGORIES.NAME
                                .eq(categoryName)));
    }
}
