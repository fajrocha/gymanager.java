package com.faroc.gymanager.application.security.authorization;

import an.awesome.pipelinr.Command;
import com.faroc.gymanager.application.security.CurrentUserProvider;
import com.faroc.gymanager.application.shared.exceptions.ForbiddenException;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Order(1)
public class AuthorizationMiddleware implements Command.Middleware {
    private final CurrentUserProvider currentUserProvider;

    public AuthorizationMiddleware(CurrentUserProvider currentUserProvider) {
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    public <R, C extends Command<R>> R invoke(C c, Next<R> next) {
        var requiredPermissions = getRequiredPermissions(c.getClass());

        if (requiredPermissions.length == 0)
            return next.invoke();

        var user = currentUserProvider.getCurrentUser();

        if (hasEnoughPermissions(requiredPermissions, user.permissions()))
            return next.invoke();

        throw new ForbiddenException(
                "User with id " + user.id() + "does not have the permissions to do the request " + c.getClass());
    }

    private static boolean hasEnoughPermissions(String[] requiredPermissions, List<String> userPermissions) {
        Set<String> userPermissionsSet = new HashSet<>(userPermissions);
        boolean permissionsValidation = false;

        for (var permission : requiredPermissions) {
            permissionsValidation = userPermissionsSet.contains(permission);
        }

        return permissionsValidation;
    }

    private static String[] getRequiredPermissions(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Authorize.class)) {
            Authorize authorize = clazz.getAnnotation(Authorize.class);
            return authorize.permissions();
        }

        return new String[0];
    }
}
