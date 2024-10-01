package com.wizardform.api.service;

import com.wizardform.api.dto.StatusDto;
import com.wizardform.api.exception.StatusNotFoundException;
import com.wizardform.api.model.Status;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StatusService {
    List<StatusDto> getAllStatus();
    Status getStatusByStatusCode(int statusCode) throws StatusNotFoundException;
    StatusDto addStatus(StatusDto statusDto);
    boolean updateStatus(StatusDto statusDto);
    void deleteStatus(int statusCode) throws StatusNotFoundException;
}
