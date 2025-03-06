package com.screener.user_service_backend.dto.response;

import lombok.*;

@Setter
@Getter
@Builder
public class GenericResponseDTO {

    private String message;

    private String status;
}
