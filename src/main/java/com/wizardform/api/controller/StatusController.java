package com.wizardform.api.controller;

import com.wizardform.api.dto.StatusDto;
import com.wizardform.api.exception.StatusNotFoundException;
import com.wizardform.api.service.StatusService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Status Controller", description = "Handles addition, updation and deletion of Status")
@RestController
@RequestMapping("api/status")
public class StatusController {

    private final StatusService statusService;

    @Autowired
    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllStatus() {
        List<StatusDto> statusDtoList = statusService.getAllStatus();
        return ResponseEntity.ok(statusDtoList);
    }

    @PostMapping(value = "add", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addNewStatus(@Valid @RequestBody StatusDto statusDto) {
        StatusDto addedStatus = statusService.addStatus(statusDto);
        return ResponseEntity.created(URI.create("/status")).body(addedStatus);
    }

    @PutMapping(value = "update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateStatus(@Valid @RequestBody StatusDto statusDto) {
        boolean isStatusUpdated = statusService.updateStatus(statusDto);
        return isStatusUpdated ? ResponseEntity.noContent().build() : ResponseEntity.badRequest().body("Status update failed");
    }

    @DeleteMapping("delete/{statusCode}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteStatus(@PathVariable int statusCode) throws StatusNotFoundException {
        statusService.deleteStatus(statusCode);
        return ResponseEntity.noContent().build();
    }
}
