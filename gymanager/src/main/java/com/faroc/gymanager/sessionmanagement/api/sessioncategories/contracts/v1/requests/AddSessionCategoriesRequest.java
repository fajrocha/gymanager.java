package com.faroc.gymanager.sessionmanagement.api.sessioncategories.contracts.v1.requests;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record AddSessionCategoriesRequest(
        @NotEmpty(message = "Session categories names cannot be empty.")
        List<String> sessionCategories) {
}
