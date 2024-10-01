package com.wizardform.api.controller;

import com.wizardform.api.dto.StatusDto;
import com.wizardform.api.exception.StatusNotFoundException;
import com.wizardform.api.service.StatusService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/status")
public class StatusController {

    private final StatusService statusService;

    @Autowired
    public StatusController(StatusService statusService) {
        this.statusService = statusService;
    }

    @GetMapping
    public ResponseEntity<?> getAllStatus() {
        List<StatusDto> statusDtoList = statusService.getAllStatus();
        return ResponseEntity.ok(statusDtoList);
    }

    @PostMapping("add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addNewStatus(@Valid @RequestBody StatusDto statusDto) {
        StatusDto addedStatus = statusService.addStatus(statusDto);
        return ResponseEntity.created(URI.create("/status")).body(addedStatus);
    }

    @PutMapping("update")
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
