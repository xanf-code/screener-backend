package com.screener.fraud_detection_service_backend.controller;

import com.screener.fraud_detection_service_backend.service.ITestService;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/fraud", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class TestServiceController {

    private final ITestService testService;

    @GetMapping("/test")
    public String test() {
        return testService.test();
    }
}
