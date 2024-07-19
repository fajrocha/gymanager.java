package com.faroc.gymanager.application.users.gateways;

import com.faroc.gymanager.domain.trainers.Trainer;

import java.util.Optional;
import java.util.UUID;

public interface TrainersGateway {
    void create(Trainer trainer);
    Optional<Trainer> findById(UUID id);
}
