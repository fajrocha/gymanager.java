package com.faroc.gymanager.usermanagement.application.users.gateways;

import com.faroc.gymanager.usermanagement.domain.users.User;

public interface TokenGenerator {
    String generate(User user);
}
