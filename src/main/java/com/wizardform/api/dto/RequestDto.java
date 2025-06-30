package com.wizardform.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {

    private long requestId;
    private long userId;
    private String title;
    private String guardianName;
    private String phone;
    private LocalDate requestDate;
    private String priority;
    private String status;
    private long documentId;
    private String callbackUrl;
}
