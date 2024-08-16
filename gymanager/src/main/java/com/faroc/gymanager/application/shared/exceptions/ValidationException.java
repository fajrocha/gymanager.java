package com.faroc.gymanager.application.shared.exceptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidationException extends RuntimeException {
    public static final String DEFAULT_DETAIL = "The provided data is invalid.";
    private String detail = DEFAULT_DETAIL;
    private Map<String, List<String>> modelState = new HashMap<>();

    public ValidationException(String message) {
        super(message);
    }
    public ValidationException(String message, String detail) {

        super(message);
        this.detail = detail;
    }

    public ValidationException(String message, Map<String, List<String>> modelState) {

        super(message);
        this.modelState = modelState;
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
    public ValidationException(Throwable cause) {
        super(cause);
    }

    public String getDetail() {
        return detail;
    }

    public Map<String, List<String>> getModelState() {
        return modelState;
    }
}
