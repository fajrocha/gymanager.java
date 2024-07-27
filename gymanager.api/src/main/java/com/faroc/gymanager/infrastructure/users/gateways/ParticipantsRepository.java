package com.faroc.gymanager.infrastructure.users.gateways;

import com.faroc.gymanager.application.users.gateways.ParticipantsGateway;
import com.faroc.gymanager.domain.participants.Participant;
import com.faroc.gymanager.infrastructure.users.mappers.ParticipantMappers;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static org.jooq.codegen.maven.gymanager.Tables.PARTICIPANTS;

@Repository
public class ParticipantsRepository implements ParticipantsGateway {
    private final DSLContext context;

    public ParticipantsRepository(DSLContext context) {
        this.context = context;
    }

    @Override
    public void create(Participant participant) {
        var participantRecord = ParticipantMappers.toRecordCreate(participant);

        context.insertInto(PARTICIPANTS).set(participantRecord).execute();
    }

    @Override
    public void update(Participant participant) {
        var participantRecord = ParticipantMappers.toRecordUpdate(participant);

        context.update(PARTICIPANTS).set(participantRecord).execute();
    }

    @Override
    public Optional<Participant> findById(UUID id) {
        var participantRecord = context.selectFrom(PARTICIPANTS).where(PARTICIPANTS.ID.eq(id)).fetchOne();

        if (participantRecord == null)
            return Optional.empty();

        var participant = ParticipantMappers.toDomain(participantRecord);

        return Optional.of(participant);
    }
}
