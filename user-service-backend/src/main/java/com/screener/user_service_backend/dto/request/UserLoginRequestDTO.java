package com.screener.user_service_backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "User Login Request Body", description = "User request data transfer object")
public class UserLoginRequestDTO {
    private String username;
    private String password;
}
