package com.faroc.gymanager.sessionmanagement.application.sessions.gateways;

import com.faroc.gymanager.sessionmanagement.domain.sessions.SessionCategory;

import java.util.List;

public interface SessionCategoriesGateway {
    void createAll(List<SessionCategory> sessionCategories);
    boolean existsByName(String name);
}
