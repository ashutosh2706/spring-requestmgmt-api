package com.wizardform.api.service;

import com.wizardform.api.Constants;
import com.wizardform.api.dto.PagedResponseDto;
import com.wizardform.api.dto.RequestDto;
import com.wizardform.api.exception.*;
import com.wizardform.api.mapper.RequestMapper;
import com.wizardform.api.model.*;
import com.wizardform.api.repository.RequestRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

@Component
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final StatusService statusService;
    private final PriorityService priorityService;
    private final UserService userService;
    private final FileService fileService;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository, PriorityService priorityService, StatusService statusService, UserService userService, FileService fileService) {
        this.requestRepository = requestRepository;
        this.priorityService = priorityService;
        this.statusService = statusService;
        this.userService = userService;
        this.fileService = fileService;
    }

    @Override
    public PagedResponseDto<RequestDto> getAllRequests(String searchTerm, int pageNumber, int pageSize, String sortField, String sortDirection) throws IllegalArgumentException {
        // default sort field is requestId if none provided
        // validation check: if the value provided in sortField is a valid property or not
        sortField = sortField.trim().isEmpty() ? "requestId" : sortField;
        if(!isValidSortField(sortField)) {
            throw new IllegalArgumentException("Invalid sort field: " + sortField);
        }

        Sort sort = Sort.by(sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, sortField);
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize, sort);

        Page<Request> requestPage = requestRepository.findAll(pageRequest);
        List<Request> requests = requestPage.getContent();
        // now map it to requestDto and return as expected
        List<RequestDto> requestDtoList = RequestMapper.INSTANCE.requestListToRequestDtoList(requests);
        return new PagedResponseDto<>(pageNumber, requestPage.getNumberOfElements(), requestPage.getTotalPages(), requestPage.getTotalElements(), requestDtoList);
    }

    @Override
    public RequestDto addNewRequest(RequestDto requestDto) throws UserNotFoundException, PriorityNotFoundException, StatusNotFoundException, IOException {
        Request newRequest = RequestMapper.INSTANCE.RequestDtoToRequest(requestDto);

        // To handle the attached file
        MultipartFile attachedFile = requestDto.getAttachedFile();
        FileDetail savedFileDetail = null;

        // new request status is always pending by default unless changed
        // priorityCode & userId will be provided by user
        User userFromDb = userService.getUserByUserId(requestDto.getUserId());
        Priority priorityFromDb = priorityService.getPriorityByPriorityCode(requestDto.getPriorityCode());
        Status statusFromDb = statusService.getStatusByStatusCode(Constants.StatusCode.STATUS_PENDING);
        if(userFromDb.isEnabled()) {
            if(attachedFile != null) {
                // call FileDetail service with multipart file argument. It will copy the file, save the details to db and return the FileDetail entity
                savedFileDetail = fileService.saveFile(attachedFile);
            }
            newRequest.setUser(userFromDb);
            newRequest.setPriority(priorityFromDb);
            newRequest.setStatus(statusFromDb);
            newRequest.setFileDetail(savedFileDetail);

            Request savedRequest = requestRepository.save(newRequest);
            return RequestMapper.INSTANCE.requestToRequestDto(savedRequest);

        } else throw new UserNotFoundException("User with id: " + requestDto.getUserId() + " doesn't exist or disabled");
    }

    @Override
    public RequestDto getRequestByRequestId(long requestId) throws RequestNotFoundException {
        Optional<Request> requestOptional = requestRepository.findByRequestId(requestId);
        if(requestOptional.isPresent()) {
            Request request = requestOptional.get();
            return RequestMapper.INSTANCE.requestToRequestDto(request);
        } else throw new RequestNotFoundException("Request with id: " + requestId + " was not found");
    }

    @Override
    @Transactional
    public RequestDto updateRequest(RequestDto requestDto) throws RequestNotFoundException, UserNotFoundException, PriorityNotFoundException, FileDetailsNotFoundException, IOException {
        Optional<Request> requestOptional = requestRepository.findByRequestId(requestDto.getRequestId());
        if(requestOptional.isPresent()) {

            MultipartFile attachedFile = requestDto.getAttachedFile();
            FileDetail newFileDetail = null;

            Priority newPriority = priorityService.getPriorityByPriorityCode(requestDto.getPriorityCode());
            User newUser = userService.getUserByUserId(requestDto.getUserId());

            Request existingRequest = requestOptional.get();
            // update the existing request with new data
            existingRequest.setTitle(requestDto.getTitle());
            existingRequest.setGuardianName(requestDto.getGuardianName());
            existingRequest.setRequestDate(requestDto.getRequestDate());
            existingRequest.setPhone(requestDto.getPhone());
            existingRequest.setUser(newUser);
            existingRequest.setPriority(newPriority);

            if(attachedFile != null) {
                // existing file will be deleted along with it's details and new one will be copied and details saved to db
                long existingFileId = existingRequest.getFileDetail().getFileId();
                fileService.deleteFileDetails(existingFileId);
                newFileDetail = fileService.saveFile(attachedFile);
                existingRequest.setFileDetail(newFileDetail);
            }

            Request updatedRequest = requestRepository.save(existingRequest);
            return RequestMapper.INSTANCE.requestToRequestDto(updatedRequest);

        } else throw new RequestNotFoundException("Request with id: " + requestDto.getRequestId() + " was not found");
    }

    @Override
    @Transactional
    public void deleteRequest(long requestId) throws RequestNotFoundException, FileDetailsNotFoundException {
        Optional<Request> requestOptional = requestRepository.findByRequestId(requestId);
        if(requestOptional.isPresent()) {
            // first delete the request and then file details (mark the order)
            requestRepository.delete(requestOptional.get());
            fileService.deleteFileDetails(requestOptional.get().getFileDetail().getFileId());
        } else throw new RequestNotFoundException("Request with id: " + requestId + " was not found");
    }

    // use reflection to validate if the given sortField is present
    private static boolean isValidSortField(String sortField) {
        for(Field field: Request.class.getDeclaredFields()) {
            if(field.getName().equals(sortField))
                return true;
        }
        return false;
    }
}
