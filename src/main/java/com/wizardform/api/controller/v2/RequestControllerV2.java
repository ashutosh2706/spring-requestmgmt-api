package com.wizardform.api.controller.v2;

import com.wizardform.api.dto.NewRequestDto;
import com.wizardform.api.exception.CallbackAbsentException;
import com.wizardform.api.exception.PriorityNotFoundException;
import com.wizardform.api.exception.StatusNotFoundException;
import com.wizardform.api.exception.UserNotFoundException;
import com.wizardform.api.service.FileService;
import com.wizardform.api.service.RequestService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@Tag(name = "Request Controller", description = "Handles operations related to request management")
@RestController
@RequestMapping("api/v2/requests")
public class RequestControllerV2 {

    private final RequestService requestService;
    private final FileService fileService;

    @Autowired
    public RequestControllerV2(RequestService requestService, FileService fileService) {
        this.requestService = requestService;
        this.fileService = fileService;
    }

    @PostMapping(
            value = "add",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> addNewRequest(@Valid @ModelAttribute NewRequestDto newRequestDto)
            throws UserNotFoundException, StatusNotFoundException, PriorityNotFoundException, IOException, CallbackAbsentException {

        if(newRequestDto.getCallbackUrl() == null || StringUtils.isBlank(newRequestDto.getCallbackUrl().trim()) ||
                StringUtils.isEmpty(newRequestDto.getCallbackUrl().trim())) {
            throw new CallbackAbsentException("Callback required for async task");
        }

        File tmp = fileService.createTmpFile(newRequestDto);
        requestService.addNewRequestAsync(newRequestDto, tmp);
        return ResponseEntity.accepted().build();
    }
}
