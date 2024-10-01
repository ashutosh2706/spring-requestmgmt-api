package com.wizardform.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    @NotNull(message = "UserId cannot be null")
    private long userId;
    @NotBlank(message = "First Name cannot be blank")
    private String firstName;
    @NotBlank(message = "Last Name cannot be blank")
    private String lastName;
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank
    private boolean isAllowed;
    private int roleId = 1;
}
