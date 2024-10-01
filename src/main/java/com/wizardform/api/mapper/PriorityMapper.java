package com.wizardform.api.mapper;

import com.wizardform.api.dto.PriorityDto;
import com.wizardform.api.model.Priority;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PriorityMapper {

    PriorityMapper INSTANCE = Mappers.getMapper(PriorityMapper.class);
    List<PriorityDto> priorityListToPriorityDtoList(List<Priority> priorities);
    Priority priorityDtoToPriority(PriorityDto priorityDto);
    PriorityDto priorityToPriorityDto(Priority priority);
}
