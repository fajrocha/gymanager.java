package com.faroc.gymanager.common.infrastructure.security;

import com.faroc.gymanager.usermanagement.domain.users.abstractions.PasswordHasher;
import com.faroc.gymanager.common.application.security.exceptions.PasswordComplexityException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class DefaultPasswordHasher implements PasswordHasher {
    private static final String PASSWORD_REGEX = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";

    @Override
    public String hashPassword(String password) {
        if (!Pattern.compile(PASSWORD_REGEX).matcher(password).matches()) {
            throw new PasswordComplexityException("Password did not match the regex rules.");
        }

        return new BCryptPasswordEncoder().encode(password);
    }

    @Override
    public boolean validatePassword(String password, String hash) {
        return new BCryptPasswordEncoder().matches(password, hash);
    }
}
