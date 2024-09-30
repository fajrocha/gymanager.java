package com.faroc.gymanager.sessionmanagement.api.sessioncategories.contracts.v1.responses;

import com.faroc.gymanager.sessionmanagement.domain.sessions.SessionCategory;

import java.util.List;

public record AddSessionCategoriesResponse(
        List<SessionCategory> sessionCategories
) {
}
