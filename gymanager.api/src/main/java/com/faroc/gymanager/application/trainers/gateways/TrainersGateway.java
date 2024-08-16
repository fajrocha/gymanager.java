package com.faroc.gymanager.application.trainers.gateways;

import com.faroc.gymanager.domain.trainers.Trainer;

import java.util.Optional;
import java.util.UUID;

public interface TrainersGateway {
    void create(Trainer trainer);
    void update(Trainer trainer);
    Optional<Trainer> findById(UUID id);
}
