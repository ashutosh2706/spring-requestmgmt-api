package com.wizardform.api.exception;

import java.io.Serial;

public class UnauthorizedServiceException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    private String errorMessage;
    private Object errorTrace;

    public UnauthorizedServiceException(String errorMessage, Object errorTrace) {
        super(errorMessage);
        this.errorTrace = errorTrace;
        this.errorMessage = errorMessage;
    }

    public UnauthorizedServiceException(String errorMessage) {
        super(errorMessage);
        this.errorMessage = errorMessage;
    }

    public UnauthorizedServiceException(String errorMessage, Object errorTrace, Throwable throwable) {
        super(errorMessage, throwable);
        this.errorMessage = errorMessage;
        this.errorTrace = errorTrace;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Object getErrorTrace() {
        return errorTrace;
    }

    public void setErrorTrace(Object errorTrace) {
        this.errorTrace = errorTrace;
    }
}
