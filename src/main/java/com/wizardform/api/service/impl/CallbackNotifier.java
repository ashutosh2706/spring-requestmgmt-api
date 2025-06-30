package com.wizardform.api.service.impl;

import com.wizardform.api.dto.WorkerResultDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class CallbackNotifier {

    private final RestTemplate restTemplate;

    @Autowired
    public CallbackNotifier(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Retryable(
            value = { ResourceAccessException.class, HttpServerErrorException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 3000, multiplier = 2)
    )
    public void notifyClient(String callbackUrl, WorkerResultDto result) {
        restTemplate.postForObject(callbackUrl, result, String.class);
    }

    @Recover
    public void recover(Exception e, String callbackUrl, WorkerResultDto result) {
        log.error("All retries failed for callback URL: {}", callbackUrl);
        System.err.println("All retries failed for callback URL: " + callbackUrl);
    }
}
