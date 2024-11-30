package com.wizardform.api.exception;

import java.io.Serial;

public class RequestNotFoundException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;
    public RequestNotFoundException(String message) {
        super(message);
    }
}
