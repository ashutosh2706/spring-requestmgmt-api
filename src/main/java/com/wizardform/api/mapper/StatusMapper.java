package com.wizardform.api.mapper;

import com.wizardform.api.dto.StatusDto;
import com.wizardform.api.model.Status;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface StatusMapper {
    StatusMapper INSTANCE = Mappers.getMapper(StatusMapper.class);
    List<StatusDto> statusListToStatusDtoList(List<Status> statusList);
    Status statusDtoToStatus(StatusDto statusDto);
    StatusDto statusToStatusDto(Status status);
}
