package com.wizardform.api.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/requests")
public class RequestController {

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getAllRequest() {
//        User user = new User();
//        UserDTO userDTO = UserMapper.INSTANCE.userToUserDTO(user);  // mapping
        return "hello";
    }

}
