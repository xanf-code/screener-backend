package com.screener.user_service_backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "Update User Password Response Body")
public class UpdatePasswordResponseDTO {

    private String message;

    private String userName;
}
