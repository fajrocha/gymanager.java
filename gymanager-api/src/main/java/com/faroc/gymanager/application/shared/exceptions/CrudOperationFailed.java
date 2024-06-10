package com.faroc.gymanager.application.shared.exceptions;

public class CrudOperationFailed extends RuntimeException {
    private String detail = "CRUD operation failed";

    public CrudOperationFailed(String message) {
        super(message);
    }
    public CrudOperationFailed(String message, String detail) {

        super(message);
        this.detail = detail;
    }
    public CrudOperationFailed(String message, Throwable cause) {
        super(message, cause);
    }
    public CrudOperationFailed(Throwable cause) {
        super(cause);
    }

    public String getDetail() {
        return detail;
    }
}
