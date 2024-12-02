package com.wizardform.api.exception;

import java.io.Serial;

public class InvalidRefreshTokenException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;
    public InvalidRefreshTokenException(String message) {
        super(message);
    }
}
