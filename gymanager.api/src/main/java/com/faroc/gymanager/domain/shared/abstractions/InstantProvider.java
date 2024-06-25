package com.faroc.gymanager.domain.shared.abstractions;

import java.time.Instant;
import java.time.ZonedDateTime;

public interface InstantProvider {
    Instant now();
}
