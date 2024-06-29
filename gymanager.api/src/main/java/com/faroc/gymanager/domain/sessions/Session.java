package com.faroc.gymanager.domain.sessions;

import com.faroc.gymanager.domain.shared.exceptions.ConflictException;
import com.faroc.gymanager.domain.shared.valueobjects.timeslots.TimeSlot;
import com.faroc.gymanager.domain.shared.Entity;
import com.faroc.gymanager.domain.shared.exceptions.UnexpectedException;
import com.faroc.gymanager.domain.participants.Participant;
import com.faroc.gymanager.domain.sessions.exceptions.CancellationTooCloseToSession;
import com.faroc.gymanager.domain.sessions.exceptions.MaxParticipantsReachedException;
import com.faroc.gymanager.domain.shared.abstractions.InstantProvider;
import jdk.jfr.Category;
import lombok.Getter;

import java.time.Duration;
import java.util.*;

public class Session extends Entity {
    public static final int MIN_CANCELLATION_HOURS = 24;

    @Getter
    private final UUID trainerId;
    @Getter
    private final TimeSlot timeSlot;
    @Getter
    private final String name;
    @Getter
    private final String description;
    @Getter
    private final int maximumNumberParticipants;
    private final Set<Reservation> reservations = new HashSet<>();
    private final List<SessionCategory> categories;

    public Session(
            UUID trainerId,
            TimeSlot timeSlot,
            String name,
            String description,
            List<SessionCategory> categories,
            int maxNumberParticipants) {
        this.trainerId = trainerId;
        this.timeSlot = timeSlot;
        this.name = name;
        this.description = description;
        this.categories = categories;
        this.maximumNumberParticipants = maxNumberParticipants;
    }

    public Session(
            UUID id,
            UUID trainerId,
            TimeSlot timeSlot,
            String name,
            String description,
            List<SessionCategory> categories,
            int maximumNumberParticipant) {
        super(id);
        this.trainerId = trainerId;
        this.timeSlot = timeSlot;
        this.name = name;
        this.description = description;
        this.categories = categories;
        this.maximumNumberParticipants = maximumNumberParticipant;
    }

    public void cancelReservation(Reservation reservation, InstantProvider instantProvider) {
        var participantId = reservation.getParticipantId();
        var now = instantProvider.now();

        var timeDifference = Duration.between(now, timeSlot.getStartTime());

        if (tooCloseToSession(timeDifference))
            throw new CancellationTooCloseToSession(
                    SessionErrors.cancellationCloseToStart(id, participantId),
                    SessionErrors.CANCELLATION_CLOSE_TO_START
            );

        if (!reservations.remove(reservation))
            throw new UnexpectedException(
                    SessionErrors.participantNotFound(id, participantId),
                    SessionErrors.PARTICIPANT_NOT_FOUND
            );
    }

    public void makeReservation(Reservation reservation) {
        var participantId = reservation.getParticipantId();

        if (reservations.size() == maximumNumberParticipants)
            throw new MaxParticipantsReachedException(
                    SessionErrors.maxParticipantsReached(id, participantId)
            );

        if (reservations.contains(reservation)) {
            throw new ConflictException(
                    SessionErrors.conflictParticipant(id, participantId),
                    SessionErrors.CONFLICT_PARTICIPANT);
        }

        reservations.add(reservation);
    }

    public boolean hasReservation(Reservation reservation) {
        return reservations.contains(reservation);
    }

    public List<SessionCategory> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    private boolean tooCloseToSession(Duration timeDifference) {
        return timeDifference.toHours() < MIN_CANCELLATION_HOURS;
    }
}
