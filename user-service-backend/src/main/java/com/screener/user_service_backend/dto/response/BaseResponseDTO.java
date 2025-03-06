package com.screener.user_service_backend.dto.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode
@ToString
public class BaseResponseDTO {
    private String statusCode;
    private String statusMessage;
}
