package com.faroc.gymanager.sessionmanagement.domain.sessions;

import com.faroc.gymanager.common.domain.Entity;
import lombok.Getter;

@Getter
public class SessionCategory extends Entity {
    private final String name;

    public SessionCategory(String name) {
        this.name = name;
    }
}
