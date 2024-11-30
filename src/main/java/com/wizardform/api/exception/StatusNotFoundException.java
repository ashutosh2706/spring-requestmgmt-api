package com.wizardform.api.exception;

import java.io.Serial;

public class StatusNotFoundException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;
    public StatusNotFoundException(String message) {
        super(message);
    }
}
