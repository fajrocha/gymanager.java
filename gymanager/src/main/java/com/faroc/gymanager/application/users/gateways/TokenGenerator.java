package com.faroc.gymanager.application.users.gateways;

import com.faroc.gymanager.domain.users.User;

public interface TokenGenerator {
    String generate(User user);
}
