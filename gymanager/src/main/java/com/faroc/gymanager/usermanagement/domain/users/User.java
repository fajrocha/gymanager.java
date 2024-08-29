package com.faroc.gymanager.usermanagement.domain.users;

import com.faroc.gymanager.common.domain.AggregateRoot;
import com.faroc.gymanager.usermanagement.domain.users.abstractions.PasswordHasher;
import com.faroc.gymanager.usermanagement.domain.users.errors.UserErrors;
import com.faroc.gymanager.common.domain.exceptions.ConflictException;
import com.faroc.gymanager.usermanagement.domain.users.events.AddAdminEvent;
import com.faroc.gymanager.usermanagement.domain.users.events.AddParticipantEvent;
import com.faroc.gymanager.usermanagement.domain.users.events.AddTrainerEvent;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class User extends AggregateRoot {
    private final UUID id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String passwordHash;
    private UUID adminId;
    private UUID participantId;
    private UUID trainerId;

    public User(
            UUID id,
            String firstName,
            String lastName,
            String email,
            String passwordHash)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public User(String firstName, String lastName, String email, String passwordHash) {
        this.id = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public boolean validatePassword(String password, PasswordHasher passwordHasher) {
        return passwordHasher.validatePassword(password, passwordHash);
    }

    public UUID createAdminProfile() {
        if (adminId != null) {
            throw new ConflictException(
                    UserErrors.conflictAdminProfile(id),
                    UserErrors.CONFLICT_ADMIN_PROFILE);
        }

        adminId = UUID.randomUUID();
        domainEvents.add(new AddAdminEvent(adminId, id));

        return adminId;
    }

    public UUID createTrainerProfile() {
        if (trainerId != null) {
            throw new ConflictException(
                    UserErrors.conflictTrainerProfile(id),
                    UserErrors.CONFLICT_TRAINER_PROFILE);
        }

        trainerId = UUID.randomUUID();
        domainEvents.add(new AddTrainerEvent(trainerId, id));

        return trainerId;
    }

    public UUID createParticipantProfile() {
        if (participantId != null) {
            throw new ConflictException(
                    UserErrors.conflictParticipantProfile(id),
                    UserErrors.CONFLICT_PARTICIPANT_PROFILE);
        }

        participantId = UUID.randomUUID();
        domainEvents.add(new AddParticipantEvent(participantId, id));

        return participantId;
    }

    public List<UserProfileTypes> getUserProfiles()
    {
        List<UserProfileTypes> userProfiles = new ArrayList<>();

        if (adminId != null)
            userProfiles.add(UserProfileTypes.ADMIN);

        if (trainerId != null)
            userProfiles.add(UserProfileTypes.TRAINER);

        if (participantId != null)
            userProfiles.add(UserProfileTypes.PARTICIPANT);

        return userProfiles;
    }

    public User mapAdminId(UUID adminId) {
        this.adminId = adminId;

        return this;
    }

    public User mapTrainerId(UUID trainerId) {
        this.trainerId = trainerId;

        return this;
    }

    public User mapParticipantId(UUID participantId) {
        this.participantId = participantId;

        return this;
    }
}
