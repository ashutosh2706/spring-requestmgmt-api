package com.wizardform.api.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.HttpStatus;

import java.io.Serial;
import java.util.Date;

public class ErrorResponse extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;
    private final Integer status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final Date timestamp;
    private final String error;
    private final String details;
    private final String message;
    private final String enquiryId = "";

    public ErrorResponse(String message, String details, HttpStatus httpStatus) {
        super();
        this.status = httpStatus.value();
        this.error = httpStatus.name();
        this.timestamp = new Date();
        this.message = message;
        this.details = details;
    }

    public ErrorResponse(String message, String details, HttpStatus httpStatus, Throwable e) {
        super();
        this.status = httpStatus.value();
        this.error = httpStatus.name();
        this.timestamp = new Date();
        this.message = message;
        this.details = details;
    }

    public ErrorResponse(String message, HttpStatus httpStatus) {
        super();
        this.status = httpStatus.value();
        this.error = httpStatus.name();
        this.timestamp = new Date();
        this.message = message;
        this.details = "";
    }

    @Override
    @JsonIgnore
    public StackTraceElement[] getStackTrace() {
        return super.getStackTrace();
    }

    public Integer getStatus() {
        return status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getError() {
        return error;
    }

    public String getDetails() {
        return details;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getEnquiryId() {
        return enquiryId;
    }
}
