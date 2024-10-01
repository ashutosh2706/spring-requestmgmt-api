package com.wizardform.api.dto;

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
public class RoleDto {
    @NotNull(message = "RoleId cannot be null")
    private int roleId;
    @NotBlank(message = "Role type cannot be blank")
    private String roleType;
}
