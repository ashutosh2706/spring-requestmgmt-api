package com.wizardform.api.service;

import com.wizardform.api.dto.NewRequestDto;
import com.wizardform.api.dto.PagedResponseDto;
import com.wizardform.api.dto.RequestDto;
import com.wizardform.api.exception.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface RequestService {
    PagedResponseDto<RequestDto> getAllRequests(String searchTerm, int pageNumber, int pageSize, String sortField, String sortDirection) throws IllegalArgumentException;
    RequestDto addNewRequest(NewRequestDto newRequestDto) throws UserNotFoundException, PriorityNotFoundException, StatusNotFoundException, IOException;
    RequestDto getRequestByRequestId(long requestId) throws RequestNotFoundException;
    void deleteRequest(long requestId) throws RequestNotFoundException, FileDetailsNotFoundException;
    PagedResponseDto<RequestDto> getAllRequestByUserId(long userId, String searchTerm, int pageNumber, int pageSize, String sortField, String sortDirection) throws IllegalArgumentException, UserNotFoundException;
    RequestDto updateRequest(NewRequestDto newRequestDto) throws RequestNotFoundException, UserNotFoundException, PriorityNotFoundException, StatusNotFoundException, FileDetailsNotFoundException, IOException;
}
