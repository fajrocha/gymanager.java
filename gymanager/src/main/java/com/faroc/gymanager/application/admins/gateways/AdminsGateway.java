package com.faroc.gymanager.application.admins.gateways;

import com.faroc.gymanager.domain.admins.Admin;

import java.util.Optional;
import java.util.UUID;

public interface AdminsGateway {
    void create(Admin admin);
    void update(Admin admin);
    Optional<Admin> findById(UUID id);
}