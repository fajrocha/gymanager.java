package com.faroc.gymanager.infrastructure.users.authentication;

import com.faroc.gymanager.application.users.gateways.TokenGenerator;
import com.faroc.gymanager.domain.admins.permissions.AdminPermissions;
import com.faroc.gymanager.domain.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class JwtTokenGenerator implements TokenGenerator {
    @Value("${spring.application.name}")
    private String issuer;

    private final JwtEncoder jwtEncoder;

    public JwtTokenGenerator(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String generate(User user) {
        Instant now = Instant.now();
        long expiry = 3600L;

        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .claims(map -> {
                    map.put(RegisteredClaims.NAME, user.getFirstName());
                    map.put(RegisteredClaims.FAMILY_NAME, user.getLastName());
                    map.put(RegisteredClaims.EMAIL, user.getEmail());
                    map.put(RegisteredClaims.ID, user.getLastName());
                });

        addRoleIds(user, claimsBuilder);
        user.getUserProfiles().forEach(profile -> {
            List<String> roles = new ArrayList<>();
            roles.add(profile.name());

            claimsBuilder.claim(RolesClaims.ROLES, roles);
        });

        addPermissions(user, claimsBuilder);

        var claims = claimsBuilder.build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public void addRoleIds(User user, JwtClaimsSet.Builder claimsBuilder) {
        if (user.getAdminId() != null) {
            claimsBuilder.claim(ProfileClaims.ADMIN_ID, user.getAdminId());
        }

        if (user.getTrainerId() != null) {
            claimsBuilder.claim(ProfileClaims.PARTICIPANT_ID, user.getTrainerId());
        }

        if (user.getParticipantId() != null) {
            claimsBuilder.claim(ProfileClaims.PARTICIPANT_ID, user.getParticipantId());
        }
    }

    public void addPermissions(User user, JwtClaimsSet.Builder claimsBuilder) {
        List<String> permissions = new ArrayList<>();

        if (user.getAdminId() != null) {
            permissions.add(AdminPermissions.CREATE_GYM);
            permissions.add(AdminPermissions.UPDATE_GYM);
        }

        if (user.getTrainerId() != null) {
            // TODO: Add trainer permissions
        }

        if (user.getParticipantId() != null) {
            // TODO: Add participant permissions
        }

        claimsBuilder.claim(PermissionsClaims.PERMISSIONS, permissions);
    }
}
