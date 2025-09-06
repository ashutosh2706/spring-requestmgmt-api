package com.wizardform.callbacklistener.client;

import java.util.Map;

@org.springframework.stereotype.Service
public class Service {

    public void logRequest(Map<String, Object> requestBody) {
        for(Map.Entry<String, Object> e: requestBody.entrySet()) {
            System.out.printf("[%s : %s]\n", e.getKey(), e.getValue());
        }
    }
}
