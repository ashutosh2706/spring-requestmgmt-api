package com.wizardform.api.exception;

import java.io.Serial;

public class RoleNotFoundException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;
    public RoleNotFoundException(String message) {
        super(message);
    }
}
