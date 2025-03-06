package com.screener.user_service_backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Verify User Request Body", description = "User request data transfer object")
public class VerifyUserDTO {
    private String email;
    private Integer verificationCode;
}
