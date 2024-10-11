package com.wizardform.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewRequestDto {

    private long requestId;
    @NotNull(message = "UserId can't be null")
    private long userId;
    @NotBlank(message = "Request Title can't be blank")
    private String title;
    @NotBlank(message = "Guardian Name can't be blank")
    private String guardianName;
    private String phone;
    @NotNull(message = "Request Date can't be null")
    private LocalDate requestDate;
    @NotNull(message = "Priority Code can't be null")
    private int priorityCode;
    private int statusCode;
    private MultipartFile attachedFile;
}
