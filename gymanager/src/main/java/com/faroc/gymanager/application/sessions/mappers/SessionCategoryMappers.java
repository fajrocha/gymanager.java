package com.faroc.gymanager.application.sessions.mappers;

import com.faroc.gymanager.application.sessions.DTOs.AddSessionCategoryDTO;
import com.faroc.gymanager.domain.sessions.SessionCategory;

public class SessionCategoryMappers {
    public static SessionCategory toDomain(AddSessionCategoryDTO sessionCategoryDTO) {
        return new SessionCategory(sessionCategoryDTO.name());
    }
}
