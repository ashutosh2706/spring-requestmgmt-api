package com.wizardform.api.controller;

import com.wizardform.api.dto.NewRequestDto;
import com.wizardform.api.dto.PagedResponseDto;
import com.wizardform.api.dto.RequestDto;
import com.wizardform.api.exception.UserNotFoundException;
import com.wizardform.api.exception.RequestNotFoundException;
import com.wizardform.api.exception.StatusNotFoundException;
import com.wizardform.api.exception.PriorityNotFoundException;
import com.wizardform.api.exception.FileDetailsNotFoundException;
import com.wizardform.api.helper.QueryParams;
import com.wizardform.api.service.RequestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;

@Tag(name = "Request Controller", description = "Handles operations related to request management")
@RestController
@RequestMapping("api/requests")
public class RequestController {

    private final RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAllRequest(@Valid @ModelAttribute QueryParams queryParams)
            throws IllegalArgumentException {
        PagedResponseDto<RequestDto> response = requestService.getAllRequests(queryParams.getSearchTerm(), queryParams.getPageNumber(), queryParams.getPageSize(), queryParams.getSortField(), queryParams.getSortDirection());
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "add", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> addNewRequest(@Valid @ModelAttribute NewRequestDto newRequestDto)
            throws UserNotFoundException, StatusNotFoundException, PriorityNotFoundException, IOException {
        RequestDto addedRequest = requestService.addNewRequest(newRequestDto);
        return ResponseEntity.created(URI.create("/requests")).body(addedRequest);
    }

    @GetMapping(value = "{requestId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getRequestByRequestId(@PathVariable long requestId)
            throws RequestNotFoundException {
        RequestDto response = requestService.getRequestByRequestId(requestId);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getAllRequestByUserId(@PathVariable long userId, @Valid @ModelAttribute QueryParams queryParams)
            throws IllegalArgumentException, UserNotFoundException {
        PagedResponseDto<RequestDto> response = requestService.getAllRequestByUserId(userId, queryParams.getSearchTerm(), queryParams.getPageNumber(), queryParams.getPageSize(), queryParams.getSortField(), queryParams.getSortDirection());
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateRequest(@Valid @ModelAttribute NewRequestDto newRequestDto)
            throws RequestNotFoundException, UserNotFoundException, PriorityNotFoundException, StatusNotFoundException, FileDetailsNotFoundException, IOException {
        RequestDto response = requestService.updateRequest(newRequestDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("delete/{requestId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> deleteRequest(@PathVariable long requestId)
            throws RequestNotFoundException, FileDetailsNotFoundException {
        requestService.deleteRequest(requestId);
        return ResponseEntity.noContent().build();
    }
}
