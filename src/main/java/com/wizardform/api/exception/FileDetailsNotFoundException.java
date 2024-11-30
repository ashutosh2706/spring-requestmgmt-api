package com.wizardform.api.exception;

import java.io.Serial;

public class FileDetailsNotFoundException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;
    public FileDetailsNotFoundException(String message) {
        super(message);
    }
}
