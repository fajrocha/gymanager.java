package com.faroc.gymanager.api.middleware;

import com.faroc.gymanager.application.shared.exceptions.CrudOperationFailed;
import com.faroc.gymanager.application.shared.exceptions.ResourceNotFoundException;
import com.faroc.gymanager.application.users.exceptions.UnauthorizedException;
import com.faroc.gymanager.domain.shared.exceptions.ConflictException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.bind.validation.ValidationErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsHandler {
    Logger logger = LogManager.getLogger(ExceptionsHandler.class);

    @ExceptionHandler(UnauthorizedException.class)
    public ProblemDetail handleUnauthorized(UnauthorizedException ex) {
        logger.error("Unauthorized user.", ex);

        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getDetail());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleConflicts(ResourceNotFoundException ex) {
        logger.error("Resource requested was not found.", ex);

        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getDetail());
    }

    @ExceptionHandler(ConflictException.class)
    public ProblemDetail handleConflicts(ConflictException ex) {
        logger.error("Conflict occurred on request.", ex);

        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getDetail());
    }

    @ExceptionHandler(CrudOperationFailed.class)
    public ProblemDetail handleConflicts(CrudOperationFailed ex) {
        logger.error("CRUD operation failed.", ex);

        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getDetail());
    }
}
