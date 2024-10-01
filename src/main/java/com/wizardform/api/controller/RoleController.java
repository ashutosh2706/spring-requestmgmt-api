package com.wizardform.api.controller;

import com.wizardform.api.dto.RoleDto;
import com.wizardform.api.exception.RoleNotFoundException;
import com.wizardform.api.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/roles")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<?> getAllRole() {
        List<RoleDto> roles = roleService.getAllRole();
        return ResponseEntity.ok(roles);
    }

    @PostMapping("add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> addRole(@Valid @RequestBody RoleDto roleDto) {
        RoleDto addedRole = roleService.addRole(roleDto);
        return ResponseEntity.created(URI.create("/roles")).body(addedRole);
    }

    @PutMapping("update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateRole(@Valid @RequestBody RoleDto roleDto) {
        boolean isRoleUpdated = roleService.updateRole(roleDto);
        return isRoleUpdated ? ResponseEntity.noContent().build() : ResponseEntity.badRequest().body("Role update failed");
    }

    @DeleteMapping("delete/{roleId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> deleteRole(@PathVariable int roleId) throws RoleNotFoundException {
        roleService.deleteRole(roleId);
        return ResponseEntity.noContent().build();
    }
}
