package com.faroc.gymanager.infrastructure.users.authentication;

import com.faroc.gymanager.domain.users.abstractions.PasswordHasher;
import com.faroc.gymanager.infrastructure.users.exceptions.PasswordRegexNotMatchedException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class DefaultPasswordHasher implements PasswordHasher {
    private static final String PASSWORD_REGEX = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";

    @Override
    public String HashPassword(String password) {
        if (!Pattern.compile(PASSWORD_REGEX).matcher(password).matches()) {
            throw new PasswordRegexNotMatchedException("Password did not match the regex rules.");
        }

        return new BCryptPasswordEncoder().encode(password);
    }

    @Override
    public boolean ValidatePassword(String password, String hash) {
        return new BCryptPasswordEncoder().matches(password, hash);
    }
}
