package com.screener.screener_api_gateway.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleCustomResponseStatusException(CustomResponseStatusException ex) {
        HttpStatus status = ex.getStatus();
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("status", status.value());
        errorBody.put("error", status.getReasonPhrase());
        errorBody.put("message", ex.getReason());
        return new ResponseEntity<>(errorBody, status);
    }
}