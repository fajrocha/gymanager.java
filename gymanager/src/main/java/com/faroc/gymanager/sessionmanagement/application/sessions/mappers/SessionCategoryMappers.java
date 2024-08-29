package com.faroc.gymanager.sessionmanagement.application.sessions.mappers;

import com.faroc.gymanager.sessionmanagement.application.sessions.dtos.AddSessionCategoryDTO;
import com.faroc.gymanager.sessionmanagement.domain.sessions.SessionCategory;

public class SessionCategoryMappers {
    public static SessionCategory toDomain(AddSessionCategoryDTO sessionCategoryDTO) {
        return new SessionCategory(sessionCategoryDTO.name());
    }
}
