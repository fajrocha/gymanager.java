package com.faroc.gymanager.common.api.middleware;

import com.faroc.gymanager.common.api.contracts.responses.ValidationProblemDetail;
import com.faroc.gymanager.common.application.exceptions.ForbiddenException;
import com.faroc.gymanager.common.application.exceptions.ResourceNotFoundException;
import com.faroc.gymanager.common.application.exceptions.ValidationException;
import com.faroc.gymanager.common.application.security.exceptions.UnauthorizedException;
import com.faroc.gymanager.common.domain.exceptions.ConflictException;
import com.faroc.gymanager.common.domain.exceptions.UnexpectedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsHandler {
    Logger logger = LogManager.getLogger(ExceptionsHandler.class);

    @ExceptionHandler(ValidationException.class)
    public ProblemDetail handleValidation(ValidationException ex) {
        logger.error("Validation failed on request.", ex);

        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getDetail());

        problem.setProperty("errors", ex.getModelState());

        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        var validationProblem = ValidationProblemDetail.create();

        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            String fieldName = fieldError.getField();
            String errorMessage = fieldError.getDefaultMessage();
            validationProblem.addValidationError(fieldName, errorMessage);
        });

        return validationProblem;
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ProblemDetail handleUnauthorized(UnauthorizedException ex) {
        logger.error("Unauthorized user.", ex);

        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getDetail());
    }

    @ExceptionHandler(ForbiddenException.class)
    public ProblemDetail handleForbiddenException(ForbiddenException ex) {
        logger.error("Forbidden user.", ex);

        return ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getDetail());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleNotFound(ResourceNotFoundException ex) {
        logger.error("Resource requested was not found.", ex);

        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getDetail());
    }

    @ExceptionHandler(ConflictException.class)
    public ProblemDetail handleConflict(ConflictException ex) {
        logger.error("Conflict occurred on request.", ex);

        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getDetail());
    }

    @ExceptionHandler(UnexpectedException.class)
    public ProblemDetail handleUnexpected(UnexpectedException ex) {
        logger.error("Unexpected behavior has occurred on request.", ex);

        return ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getDetail());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        logger.error("Failed to properly deserialize the request.", ex);

        return ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
    }
}
