package com.wizardform.api.exception;

import java.io.Serial;

public class ExpiredRefreshTokenException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;
    public ExpiredRefreshTokenException(String message) {
        super(message);
    }
}
