package com.faroc.gymanager.common.application.exceptions;

public class StoreRequestFailed extends RuntimeException {
    private String detail = "Operation requested failed.";

    public StoreRequestFailed(String message) {
        super(message);
    }
    public StoreRequestFailed(String message, String detail) {

        super(message);
        this.detail = detail;
    }
    public StoreRequestFailed(String message, Throwable cause) {
        super(message, cause);
    }
    public StoreRequestFailed(Throwable cause) {
        super(cause);
    }

    public String getDetail() {
        return detail;
    }
}
