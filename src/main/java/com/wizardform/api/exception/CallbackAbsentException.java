package com.wizardform.api.exception;

import java.io.Serial;

public class CallbackAbsentException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    public CallbackAbsentException(String message) {
        super(message);
    }
}
