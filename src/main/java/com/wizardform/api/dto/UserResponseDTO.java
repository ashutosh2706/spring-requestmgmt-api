package com.wizardform.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("user_id")
    @NotNull(message = "UserId cannot be null")
    private long userId;
    @JsonProperty("first_name")
    @NotBlank(message = "First Name cannot be blank")
    private String firstName;
    @JsonProperty("last_name")
    @NotBlank(message = "Last Name cannot be blank")
    private String lastName;
    @JsonProperty("email")
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank
    @JsonProperty("is_allowed")
    private boolean isAllowed;
    private int roleId = 1;
}
