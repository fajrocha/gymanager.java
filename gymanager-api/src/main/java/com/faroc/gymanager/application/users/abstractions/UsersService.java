package com.faroc.gymanager.application.users.abstractions;

import java.util.UUID;

public interface UsersService {
    UUID createAdminProfile(UUID adminId);
}
