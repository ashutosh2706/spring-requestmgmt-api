package com.wizardform.api.mapper;

import com.wizardform.api.dto.NewRequestDto;
import com.wizardform.api.dto.RequestDto;
import com.wizardform.api.model.Request;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface RequestMapper {

    RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);
    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "priority.description", target = "priority")
    @Mapping(source = "status.description", target = "status")
    @Mapping(source = "fileDetail.fileId", target = "documentId")
    List<RequestDto> requestListToRequestDtoList(List<Request> requests);

    @Mapping(target = "requestId", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "priority", ignore = true)
    @Mapping(target = "fileDetail", ignore = true)
    Request newRequestDtoToRequest(NewRequestDto newRequestDto);

    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "priority.description", target = "priority")
    @Mapping(source = "status.description", target = "status")
    @Mapping(source = "fileDetail.fileId", target = "documentId")
    @Mapping(target = "callbackUrl", ignore = true)
    RequestDto requestToRequestDto(Request request);

}
