package com.wizardform.api.exception;

import com.wizardform.api.helper.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ValidationErrorResponse errorResponse = new ValidationErrorResponse("One or more validation errors occurred", errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleRoleNotFoundException(RoleNotFoundException e) {
        return new ResponseEntity<>(Utils.createErrorResponse("RoleNotFoundException", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException e) {
        return new ResponseEntity<>(Utils.createErrorResponse("UserNotFoundException", e.getMessage()), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(StatusNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleStatusNotFoundException(StatusNotFoundException e) {
        return new ResponseEntity<>(Utils.createErrorResponse("StatusNotFoundException", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PriorityNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handlePriorityNotFoundException(Exception e) {
        return new ResponseEntity<>(Utils.createErrorResponse("PriorityNotFoundException", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileDetailsNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Object> handleFileDetailsNotFoundException(Exception e) {
        return new ResponseEntity<>(Utils.createErrorResponse("FileDetailsNotFoundException", e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RequestNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleRequestNotFoundException(Exception e) {
        return new ResponseEntity<>(Utils.createErrorResponse("RequestNotFoundException", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseEntity<Object> handleHttpRequestMethodNotSupportedException(Exception e) {
        return new ResponseEntity<>(Utils.createErrorResponse("HttpRequestMethodNotSupportedException", e.getMessage()), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(Exception e) {
        return new ResponseEntity<>(Utils.createErrorResponse("HttpMessageNotReadableException", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<Object> handleAuthorizationDeniedException(Exception e) {
        return new ResponseEntity<>(Utils.createErrorResponse("AuthorizationDeniedException", e.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleGeneralException(Exception e) {
        e.printStackTrace();
        System.err.println(e.getMessage());
        return new ResponseEntity<>(Utils.createErrorResponse("Internal Server Error", "Check error log for details"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
