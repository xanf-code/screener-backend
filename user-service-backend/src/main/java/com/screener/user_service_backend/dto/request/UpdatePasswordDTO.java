package com.screener.user_service_backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Update User Password Request Body", description = "User request data transfer object")
public class UpdatePasswordDTO {

    @NonNull
    private String Token;

    @NonNull
    private String newPassword;
}
