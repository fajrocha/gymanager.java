package com.faroc.gymanager.domain.sessions;

import com.faroc.gymanager.domain.timeslots.TimeSlot;
import com.faroc.gymanager.domain.shared.strategicpatterns.Entity;
import com.faroc.gymanager.domain.shared.exceptions.UnexpectedException;
import com.faroc.gymanager.domain.participants.Participant;
import com.faroc.gymanager.domain.sessions.errors.SessionErrors;
import com.faroc.gymanager.domain.sessions.exceptions.CancellationTooCloseToSession;
import com.faroc.gymanager.domain.sessions.exceptions.MaxParticipantsReachedException;
import com.faroc.gymanager.domain.shared.abstractions.InstantProvider;
import lombok.Getter;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class Session extends Entity {
    public static final int MIN_CANCELLATION_HOURS = 24;

    private final UUID trainerId;
    private final TimeSlot timeSlot;
    private final Set<UUID> participantsIds = new HashSet<>();
    private final int maximumNumberParticipant;

    public Session(
            UUID trainerId,
            TimeSlot timeSlot,
            int maxNumberParticipants) {
        super();
        this.trainerId = trainerId;
        this.timeSlot = timeSlot;
        this.maximumNumberParticipant = maxNumberParticipants;
    }

    public Session(UUID id, UUID trainerId, TimeSlot timeSlot, int maximumNumberParticipant) {
        super(id);
        this.trainerId = trainerId;
        this.timeSlot = timeSlot;
        this.maximumNumberParticipant = maximumNumberParticipant;
    }

    public void cancelReservation(Participant participant, InstantProvider instantProvider) {
        var participantId = participant.getId();
        var now = instantProvider.now();

        var timeDifference = Duration.between(now, timeSlot.getStartTime());

        if (tooCloseToSession(timeDifference))
            throw new CancellationTooCloseToSession(
                    SessionErrors.cancellationCloseToStart(id, participantId),
                    SessionErrors.CANCELLATION_CLOSE_TO_START
            );

        if (!participantsIds.remove(participantId))
            throw new UnexpectedException(
                    SessionErrors.participantNotFound(id, participantId),
                    SessionErrors.PARTICIPANT_NOT_FOUND
            );
    }

    public void makeReservation(Participant participant) {
        var participantId = participant.getId();
        if (participantsIds.size() == maximumNumberParticipant)
            throw new MaxParticipantsReachedException(
                    SessionErrors.maxParticipantsReached(id, participantId)
            );

        participantsIds.add(participantId);
    }

    public boolean hasParticipant(Participant participant) {
        return participantsIds.contains(participant.getId());
    }


    private boolean tooCloseToSession(Duration timeDifference) {
        return timeDifference.toHours() < MIN_CANCELLATION_HOURS;
    }
}
