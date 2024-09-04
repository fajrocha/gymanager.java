package com.faroc.gymanager.sessionmanagement.api.sessions.contracts.v1.requests;

import java.util.List;

public record AddSessionCategoryRequest(List<String> sessionCategories) {
}
