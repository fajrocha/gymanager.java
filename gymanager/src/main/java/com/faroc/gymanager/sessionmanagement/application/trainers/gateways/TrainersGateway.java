package com.faroc.gymanager.sessionmanagement.application.trainers.gateways;

import com.faroc.gymanager.sessionmanagement.domain.trainers.Trainer;

import java.util.Optional;
import java.util.UUID;

public interface TrainersGateway {
    void create(Trainer trainer);
    void update(Trainer trainer);
    Optional<Trainer> findById(UUID id);
}
