package com.faroc.gymanager.sessionmanagement.application.participants.gateways;

import com.faroc.gymanager.sessionmanagement.domain.participants.Participant;

import java.util.Optional;
import java.util.UUID;

public interface ParticipantsGateway {
    void create(Participant participant);
    void update(Participant participant);
    Optional<Participant> findById(UUID id);
}
