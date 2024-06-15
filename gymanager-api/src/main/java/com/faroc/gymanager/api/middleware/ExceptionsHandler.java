package com.faroc.gymanager.api.middleware;

import com.faroc.gymanager.application.shared.exceptions.StoreRequestFailed;
import com.faroc.gymanager.application.shared.exceptions.ResourceNotFoundException;
import com.faroc.gymanager.application.shared.exceptions.ValidationException;
import com.faroc.gymanager.application.security.exceptions.UnauthorizedException;
import com.faroc.gymanager.domain.shared.exceptions.ConflictException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsHandler {
    Logger logger = LogManager.getLogger(ExceptionsHandler.class);

    @ExceptionHandler(ValidationException.class)
    public ProblemDetail handleConflicts(ValidationException ex) {
        logger.error("Validation failed on request.", ex);

        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getDetail());

        problem.setProperty("errors", ex.getModelState());

        return problem;
    }

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

    @ExceptionHandler(StoreRequestFailed.class)
    public ProblemDetail handleConflicts(StoreRequestFailed ex) {
        logger.error("CRUD operation failed.", ex);

        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getDetail());
    }
}
