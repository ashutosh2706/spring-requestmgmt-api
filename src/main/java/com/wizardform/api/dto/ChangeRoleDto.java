package com.wizardform.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangeRoleDto {
    @NotNull(message = "UserId cannot be null")
    private long userId;
    @NotNull(message = "RoleId cannot be null")
    private int roleId;
}
