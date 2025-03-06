package com.screener.user_service_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class EmailOrUsernameAlreadyExistsException extends RuntimeException {

    public EmailOrUsernameAlreadyExistsException(String message) {
        super(message);
    }
}

