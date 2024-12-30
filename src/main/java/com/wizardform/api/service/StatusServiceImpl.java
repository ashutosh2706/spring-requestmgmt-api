package com.wizardform.api.service;

import com.wizardform.api.dto.StatusDto;
import com.wizardform.api.exception.StatusNotFoundException;
import com.wizardform.api.mapper.StatusMapper;
import com.wizardform.api.model.Status;
import com.wizardform.api.repository.StatusRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class StatusServiceImpl implements StatusService {

    private final StatusRepository statusRepository;

    @Autowired
    public StatusServiceImpl(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Override
    public List<StatusDto> getAllStatus() {
        List<Status> statusList = statusRepository.findAll();
        return StatusMapper.INSTANCE.statusListToStatusDtoList(statusList);
    }

    @Override
    public Status getStatusByStatusCode(int statusCode) throws StatusNotFoundException {
        Optional<Status> status = statusRepository.findByStatusCode(statusCode);
        if(status.isPresent()) {
            return status.get();
        } else {
            log.error("StatusNotFoundException: No status found with statusCode {}", statusCode);
            throw new StatusNotFoundException("Status code: " + statusCode + " was not found");
        }
    }

    @Override
    public StatusDto addStatus(StatusDto statusDto) {
        Status status = StatusMapper.INSTANCE.statusDtoToStatus(statusDto);
        Status savedStatus = statusRepository.save(status);
        return StatusMapper.INSTANCE.statusToStatusDto(savedStatus);
    }

    @Override
    @Transactional
    public boolean updateStatus(StatusDto statusDto) {
        Status providedStatus = StatusMapper.INSTANCE.statusDtoToStatus(statusDto);
        Optional<Status> existingStatusOptional = statusRepository.findByStatusCode(providedStatus.getStatusCode());
        if(existingStatusOptional.isPresent()) {
            Status existingStatus = existingStatusOptional.get();
            existingStatus.setDescription(providedStatus.getDescription());
            statusRepository.save(existingStatus);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public void deleteStatus(int statusCode) throws StatusNotFoundException {
        Optional<Status> existingStatus = statusRepository.findByStatusCode(statusCode);
        if(existingStatus.isPresent()) {
            statusRepository.delete(existingStatus.get());
        } else {
            log.error("StatusNotFoundException: No status found with statusCode {}", statusCode);
            throw new StatusNotFoundException("Status code: " + statusCode + " was not found");
        }
    }

}
