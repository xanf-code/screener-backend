package com.screener.user_service_backend.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetVerificationCodeRequestDTO {

    @NotEmpty(message = "Email cannot be empty")
    private String email;
}
