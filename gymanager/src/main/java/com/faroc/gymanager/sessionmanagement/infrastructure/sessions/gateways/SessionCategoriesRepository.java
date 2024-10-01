package com.faroc.gymanager.sessionmanagement.infrastructure.sessions.gateways;

import com.faroc.gymanager.sessionmanagement.application.sessions.gateways.SessionCategoriesGateway;
import com.faroc.gymanager.sessionmanagement.domain.sessions.SessionCategory;
import org.jooq.DSLContext;
import org.jooq.codegen.maven.gymanager.tables.records.SessionCategoriesRecord;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public void exceptExisting(List<String> sessionCategories) {
        List<String> existingEntries =
                context.select(SESSION_CATEGORIES.NAME)
                        .from(SESSION_CATEGORIES)
                        .where(SESSION_CATEGORIES.NAME.in(sessionCategories))
                        .fetch(SESSION_CATEGORIES.NAME);

        sessionCategories.removeAll(existingEntries);
    }

    @Override
    public boolean existsByName(String categoryName) {
        return context.fetchExists(
                context.selectFrom(SESSION_CATEGORIES)
                        .where(SESSION_CATEGORIES.NAME.eq(categoryName)));
    }

    @Override
    public Optional<SessionCategory> findByName(String name) {
        var sessionCategoryRecord = context.selectFrom(SESSION_CATEGORIES)
                .where(SESSION_CATEGORIES.NAME.eq(name))
                .fetchOne();

        if (sessionCategoryRecord == null) return Optional.empty();

        var sessionCategory = new SessionCategory(sessionCategoryRecord.getId(), sessionCategoryRecord.getName());

        return Optional.of(sessionCategory);
    }

    @Override
    public boolean exists(UUID id) {
        return context.fetchExists(
                context.selectFrom(SESSION_CATEGORIES)
                        .where(SESSION_CATEGORIES.ID.eq(id)));
    }

    @Override
    public boolean existsAll(List<UUID> ids) {
        var count = context.selectCount()
                .from(SESSION_CATEGORIES)
                .where(SESSION_CATEGORIES.ID.in(ids))
                .fetchOne(0, Integer.class);

        if (count == null) return false;

        return count == ids.size();
    }

    @Override
    public void deleteAll(List<UUID> ids) {
        context.deleteFrom(SESSION_CATEGORIES)
                .where(SESSION_CATEGORIES.ID.in(ids))
                .execute();
    }
}
