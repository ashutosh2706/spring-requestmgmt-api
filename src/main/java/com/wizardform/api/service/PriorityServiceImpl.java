package com.wizardform.api.service;

import com.wizardform.api.dto.PriorityDto;
import com.wizardform.api.exception.PriorityNotFoundException;
import com.wizardform.api.mapper.PriorityMapper;
import com.wizardform.api.model.Priority;
import com.wizardform.api.repository.PriorityRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PriorityServiceImpl implements PriorityService {

    private final PriorityRepository priorityRepository;

    @Autowired
    public PriorityServiceImpl(PriorityRepository priorityRepository) {
        this.priorityRepository = priorityRepository;
    }

    @Override
    public List<PriorityDto> getAllPriority() {
        List<Priority> priorities = priorityRepository.findAll();
        return PriorityMapper.INSTANCE.priorityListToPriorityDtoList(priorities);
    }

    @Override
    public Priority getPriorityByPriorityCode(int priorityCode) throws PriorityNotFoundException {
        Optional<Priority> priority = priorityRepository.findByPriorityCode(priorityCode);
        if(priority.isPresent()) {
            return priority.get();
        } else throw new PriorityNotFoundException("Priority with code: " + priorityCode + " was not found");
    }

    @Override
    public PriorityDto addPriority(PriorityDto priorityDto) {
        Priority priority = PriorityMapper.INSTANCE.priorityDtoToPriority(priorityDto);
        Priority addedPriority = priorityRepository.save(priority);
        return PriorityMapper.INSTANCE.priorityToPriorityDto(addedPriority);
    }

    @Override
    @Transactional
    public boolean updatePriority(PriorityDto priorityDto) {
        Priority providedPriority = PriorityMapper.INSTANCE.priorityDtoToPriority(priorityDto);
        Optional<Priority> existingPriorityOptional = priorityRepository.findByPriorityCode(providedPriority.getPriorityCode());
        if(existingPriorityOptional.isPresent()) {
            Priority existingPriority = existingPriorityOptional.get();
            // PriorityCode will remain same
            existingPriority.setDescription(providedPriority.getDescription());
            priorityRepository.save(existingPriority);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public void deletePriority(int priorityCode) throws PriorityNotFoundException {
        Optional<Priority> existingPriority = priorityRepository.findByPriorityCode(priorityCode);
        if(existingPriority.isPresent()) {
            priorityRepository.delete(existingPriority.get());
        } else throw new PriorityNotFoundException("Priority with code: " + priorityCode + " was not found");
    }
}
