package com.wizardform.api.service;

import com.wizardform.api.dto.PriorityDto;
import com.wizardform.api.exception.PriorityNotFoundException;
import com.wizardform.api.model.Priority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PriorityService {

    List<PriorityDto> getAllPriority();
    Priority getPriorityByPriorityCode(int priorityCode) throws PriorityNotFoundException;
    PriorityDto addPriority(PriorityDto priorityDto);
    boolean updatePriority(PriorityDto priorityDto);
    void deletePriority(int priorityCode) throws PriorityNotFoundException;
}
