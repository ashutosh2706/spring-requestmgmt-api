package com.wizardform.api.exception;

import java.io.Serial;

public class PriorityNotFoundException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;
    public PriorityNotFoundException(String message) {
        super(message);
    }
}
