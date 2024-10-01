package com.faroc.gymanager.sessionmanagement.domain.sessions;

import com.faroc.gymanager.common.domain.Entity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.UUID;

@Getter
public class SessionCategory extends Entity {
    private final String name;

    public SessionCategory(
            String name) {
        this.name = name;
    }

    public SessionCategory(
            @JsonProperty("id") UUID id,
            @JsonProperty("name") String name) {
        super(id);
        this.name = name;
    }
}
