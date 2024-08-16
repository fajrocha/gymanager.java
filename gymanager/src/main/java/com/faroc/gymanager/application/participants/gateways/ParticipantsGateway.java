package com.faroc.gymanager.application.participants.gateways;

import com.faroc.gymanager.domain.participants.Participant;

import java.util.Optional;
import java.util.UUID;

public interface ParticipantsGateway {
    void create(Participant participant);
    void update(Participant participant);
    Optional<Participant> findById(UUID id);
}
