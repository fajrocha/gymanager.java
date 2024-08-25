package com.faroc.gymanager.sessionmanagement.domain.sessions;

import com.faroc.gymanager.common.domain.Entity;
import lombok.Getter;

import java.util.UUID;

@Getter
public class SessionCategory extends Entity {
    private final String name;

    public SessionCategory(String name) {
        this.name = name;
    }
    public SessionCategory(UUID id, String name) {
        super(id);
        this.name = name;
    }

    public SessionCategory(long id, String name) {
        this.name = name;
    }
}
