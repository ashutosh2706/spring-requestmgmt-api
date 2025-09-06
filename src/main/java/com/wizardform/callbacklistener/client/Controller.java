package com.wizardform.callbacklistener.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/client-callback")
public class Controller {

    @Autowired
    private Service callbackService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public String callbackListener(@RequestBody Map<String, Object> o) {
        this.callbackService.logRequest(o);
        return "OK";
    }
}
