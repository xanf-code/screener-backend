package com.screener.user_service_backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Verify User Request Body", description = "User request data transfer object")
public class VerifyUserDTO {

    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotNull(message = "Verification code cannot be empty")
    private Integer verificationCode;
}
