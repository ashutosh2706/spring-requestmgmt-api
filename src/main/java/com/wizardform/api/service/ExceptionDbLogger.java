package com.wizardform.api.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface ExceptionDbLogger {
    void logExceptionToDB(Map<String, Object> log);
}
