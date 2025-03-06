package com.screener.fraud_detection_service_backend.service.impl;

import com.screener.fraud_detection_service_backend.service.ITestService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class TestServiceImpl implements ITestService {

    @Override
    public String test() {
        return "This is Fraud Detection Service";
    }
}
