package com.screener.user_service_backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Schema(name = "User Response Body", description = "User response DTO")
public class UserRegisterResponseDTO extends BaseResponseDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
}
