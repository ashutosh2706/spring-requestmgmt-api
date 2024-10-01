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
public class StatusDto {
    @NotNull(message = "Status code cannot be null")
    private int statusCode;
    @NotBlank(message = "Status description cannot be empty")
    private String description;
}
