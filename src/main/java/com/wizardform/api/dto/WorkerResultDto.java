package com.wizardform.api.dto;

import java.time.LocalDateTime;

public record WorkerResultDto(
        int resultId,
        String resultType,
        LocalDateTime createdAt
) {}
