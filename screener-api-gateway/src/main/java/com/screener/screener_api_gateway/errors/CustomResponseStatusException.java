package com.screener.screener_api_gateway.errors;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomResponseStatusException extends RuntimeException {
    private final HttpStatus status;
    private final String reason;

    public CustomResponseStatusException(HttpStatus status, String reason) {
        super(reason);
        this.status = status;
        this.reason = reason;
    }

}
