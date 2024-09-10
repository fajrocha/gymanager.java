package com.faroc.gymanager.common.api.contracts.responses;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ValidationProblemDetail extends ProblemDetail {
    private static final String VALIDATION_ERROR_DETAIL = "The provided data is invalid.";

    private final Map<String, String> errors = new HashMap<>();

    public void addValidationError(String key, String value) {
        errors.put(key, value);
    }

    public static ValidationProblemDetail create() {
        ValidationProblemDetail validationProblemDetail = new ValidationProblemDetail();

        validationProblemDetail.setStatus(HttpStatus.BAD_REQUEST);
        validationProblemDetail.setDetail(VALIDATION_ERROR_DETAIL);

        return validationProblemDetail;
    }
}
