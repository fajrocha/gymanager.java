package com.faroc.gymanager.usermanagement.domain.users.abstractions;

public interface PasswordHasher {
    String hashPassword(String password);
    boolean validatePassword(String password, String hash);
}
