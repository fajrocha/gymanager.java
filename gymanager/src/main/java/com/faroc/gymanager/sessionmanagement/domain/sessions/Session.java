package com.faroc.gymanager.sessionmanagement.domain.sessions;

import com.faroc.gymanager.sessionmanagement.domain.sessions.events.MakeReservationEvent;
import com.faroc.gymanager.common.domain.AggregateRoot;
import com.faroc.gymanager.common.domain.exceptions.ConflictException;
import com.faroc.gymanager.sessionmanagement.domain.common.time.TimeUtils;
import com.faroc.gymanager.sessionmanagement.domain.common.timeslots.TimeSlot;
import com.faroc.gymanager.common.domain.exceptions.UnexpectedException;
import com.faroc.gymanager.sessionmanagement.domain.sessions.exceptions.CancellationTooCloseToSession;
import com.faroc.gymanager.sessionmanagement.domain.sessions.exceptions.MaxParticipantsReachedException;
import com.faroc.gymanager.common.domain.abstractions.InstantProvider;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
public class Session extends AggregateRoot {

    public static final int MIN_CANCELLATION_HOURS = 24;

    @Getter
    private final UUID trainerId;
    @Getter
    private final UUID roomId;
    @Getter
    private final TimeSlot timeSlot;
    @Getter
    private final String name;
    @Getter
    private final String description;
    @Getter
    private final int maximumNumberParticipants;
    @Getter
    private final LocalDate date;
    private final Set<SessionReservation> reservations = new HashSet<>();

    @Getter
    private final String category;

    public Session(
            UUID trainerId,
            UUID roomId,
            TimeSlot timeSlot,
            String name,
            String description,
            String category,
            int maxNumberParticipants) {
        this.trainerId = trainerId;
        this.roomId = roomId;
        this.timeSlot = timeSlot;
        this.name = name;
        this.description = description;
        this.category = category;
        this.maximumNumberParticipants = maxNumberParticipants;
        this.date = TimeUtils.toLocalDateUtcFromInstant(timeSlot.getStartTime());
    }

    public Session(
            @JsonProperty("id") UUID id,
            @JsonProperty("trainerId") UUID trainerId,
            @JsonProperty("roomId") UUID roomId,
            @JsonProperty("timeSlot") TimeSlot timeSlot,
            @JsonProperty("name") String name,
            @JsonProperty("description") String description,
            @JsonProperty("category") String category,
            @JsonProperty("maximumNumberParticipant") int maximumNumberParticipant) {
        super(id);
        this.trainerId = trainerId;
        this.roomId = roomId;
        this.timeSlot = timeSlot;
        this.name = name;
        this.description = description;
        this.category = category;
        this.maximumNumberParticipants = maximumNumberParticipant;
        this.date = TimeUtils.toLocalDateUtcFromInstant(timeSlot.getStartTime());
    }

    public void cancelReservation(SessionReservation reservation, InstantProvider instantProvider) {
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

    public void makeReservation(SessionReservation reservation) {
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
        domainEvents.add(new MakeReservationEvent(this, reservation));
    }

    public boolean hasReservation(SessionReservation reservation) {
        return reservations.contains(reservation);
    }
    public boolean hasReservationForParticipant(UUID participantId) {

        return reservations.stream().anyMatch(reservation -> reservation.getParticipantId().equals(participantId));
    }

    public Set<SessionReservation> getReservations() {
        return Collections.unmodifiableSet(reservations);
    }

    private boolean tooCloseToSession(Duration timeDifference) {
        return timeDifference.toHours() < MIN_CANCELLATION_HOURS;
    }
}
