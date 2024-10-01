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
public class PriorityDto {
    @NotNull(message = "Priority code cannot be null")
    private int priorityCode;
    @NotBlank(message = "Priority description cannot be blank")
    private String description;
}
