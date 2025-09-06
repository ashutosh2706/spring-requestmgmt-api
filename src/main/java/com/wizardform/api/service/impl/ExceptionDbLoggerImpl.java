package com.wizardform.api.service.impl;

import com.wizardform.api.model.ExceptionDbLog;
import com.wizardform.api.repository.ExceptionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ExceptionDbLoggerImpl implements com.wizardform.api.service.ExceptionDbLogger {

    @Autowired
    private ExceptionLogRepository exceptionLogRepository;

    public void logExceptionToDB(Map<String, Object> log) {
        ExceptionDbLog dbLogModel = new ExceptionDbLog();
        dbLogModel.setError(log.getOrDefault("error", "null").toString());
        dbLogModel.setDetails(log.getOrDefault("details", "null").toString());
        dbLogModel.setEnquiryId(log.getOrDefault("enquiryId", "null").toString());
        dbLogModel.setStatus(log.getOrDefault("status", "null").toString());
        dbLogModel.setPath(log.getOrDefault("path", null).toString());
        this.exceptionLogRepository.save(dbLogModel);
    }
}
