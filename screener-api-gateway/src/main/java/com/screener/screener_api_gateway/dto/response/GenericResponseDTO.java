package com.screener.screener_api_gateway.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class GenericResponseDTO {

    private String message;

    private String status;
}
