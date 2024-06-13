package com.faroc.gymanager.domain.users;

import com.faroc.gymanager.domain.users.abstractions.PasswordHasher;
import com.faroc.gymanager.domain.users.errors.UserErrors;
import com.faroc.gymanager.domain.shared.exceptions.ConflictException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String passwordHash;
    private UUID adminId;
    private UUID participantId;
    private UUID trainerId;

    private User(
            UUID id,
            String firstName,
            String lastName,
            String email,
            String passwordHash,
            UUID adminId,
            UUID trainerId,
            UUID participantId)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.adminId = adminId;
        this.trainerId = trainerId;
        this.participantId = participantId;
    }

    public User(String firstName, String lastName, String email, String passwordHash) {
        this.id = UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public static User MapFromStorage(
            UUID id,
            String firstName,
            String lastName,
            String email,
            String passwordHash,
            UUID adminId,
            UUID trainerId,
            UUID participantId) {
        return new User(id, firstName, lastName, email, passwordHash, adminId, trainerId, participantId);
    }

    public boolean validatePassword(String password, PasswordHasher passwordHasher) {
        return passwordHasher.validatePassword(password, passwordHash);
    }

    public UUID createAdminProfile() throws ConflictException {
        if (adminId != null) {
            throw new ConflictException(
                    UserErrors.conflictAdminProfile(id),
                    UserErrors.CONFLICT_ADMIN_PROFILE);
        }

        adminId = UUID.randomUUID();

        return adminId;
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

    public UUID getId() {
        return id;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getEmail() {
        return email;
    }
    public String getPasswordHash() {
        return passwordHash;
    }
    public UUID getAdminId() {
        return adminId;
    }
    public UUID getTrainerId() {
        return trainerId;
    }
    public UUID getParticipantId() {
        return participantId;
    }
}
