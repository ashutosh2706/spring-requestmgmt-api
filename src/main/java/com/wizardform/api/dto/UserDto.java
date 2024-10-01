package com.wizardform.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private long userId;
    @NotBlank(message = "First Name cannot be blank")
    private String firstName;
    @NotBlank(message = "Last Name cannot be blank")
    private String lastName;
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank(message = "Password cannot be blank")
    private String password;
    private Boolean isActive = false;
    private int roleId = 1;
}
