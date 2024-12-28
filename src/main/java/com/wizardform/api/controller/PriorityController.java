package com.wizardform.api.controller;

import com.wizardform.api.dto.PriorityDto;
import com.wizardform.api.exception.PriorityNotFoundException;
import com.wizardform.api.service.PriorityService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Priority Controller", description = "Handles addition, updation and deletion of Priority")
@RestController
@RequestMapping("api/priority")
public class PriorityController {

    private final PriorityService priorityService;

    @Autowired
    public PriorityController(PriorityService priorityService) {
        this.priorityService = priorityService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllPriority() {
        List<PriorityDto> priorityDtoList = priorityService.getAllPriority();
        return ResponseEntity.ok(priorityDtoList);
    }

    @PostMapping(value = "add", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addNewPriority(@Valid @RequestBody PriorityDto priorityDto) {
        PriorityDto addedPriority = priorityService.addPriority(priorityDto);
        return ResponseEntity.created(URI.create("/priority")).body(addedPriority);
    }

    @PutMapping(value = "update", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updatePriority(@Valid @RequestBody PriorityDto priorityDto) {
        boolean isPriorityUpdated = priorityService.updatePriority(priorityDto);
        return isPriorityUpdated ? ResponseEntity.noContent().build() : ResponseEntity.badRequest().body("Priority update failed");
    }

    @DeleteMapping("delete/{priorityCode}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deletePriority(@PathVariable int priorityCode) throws PriorityNotFoundException {
        priorityService.deletePriority(priorityCode);
        return ResponseEntity.noContent().build();
    }
}
