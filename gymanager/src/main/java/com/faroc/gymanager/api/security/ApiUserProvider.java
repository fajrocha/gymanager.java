package com.faroc.gymanager.api.security;

import com.faroc.gymanager.application.security.CurrentUserProvider;
import com.faroc.gymanager.application.security.DTOs.CurrentUserDTO;
import com.faroc.gymanager.infrastructure.security.claims.PermissionsClaims;
import com.faroc.gymanager.infrastructure.security.claims.RegisteredClaims;
import com.faroc.gymanager.infrastructure.security.claims.RolesClaims;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ApiUserProvider implements CurrentUserProvider {

    @Override
    public CurrentUserDTO getCurrentUser() {
        var principal = (Jwt)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID userId = UUID.fromString(principal.getClaim(RegisteredClaims.ID));
        List<String> permissions = principal.getClaim(PermissionsClaims.PERMISSIONS);
        List<String> roles = principal.getClaim(RolesClaims.ROLES);

        return new CurrentUserDTO(userId, permissions, roles);
    }
}
