package com.faroc.gymanager.common.domain;

import java.util.List;

public abstract class ValueObject {
    public abstract List<Object> getEqualityComponents();

    @Override
    public int hashCode() {
        return getEqualityComponents().stream()
                .map(Object::hashCode)
                .reduce(0, (x, y) -> x ^ y);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != this.getClass())
            return false;

        return ((ValueObject)obj).getEqualityComponents().equals(getEqualityComponents());
    }
}
