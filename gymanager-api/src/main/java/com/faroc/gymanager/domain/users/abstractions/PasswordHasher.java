package com.faroc.gymanager.domain.users.abstractions;

import java.util.Optional;

public interface PasswordHasher {
    String HashPassword(String password);
    boolean ValidatePassword(String password, String hash);
}
