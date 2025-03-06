package com.screener.user_service_backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(name = "User Response Body", description = "User response DTO")
public class UserLoginResponseDTO extends BaseResponseDTO {
    private String username;
    private String jwtToken;
    private Long expirationTime;
}
