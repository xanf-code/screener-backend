package com.screener.user_service_backend.dto.rabbit;

import com.screener.user_service_backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmailQueueDTO {

    private String email;
    private Integer verificationCode;
}
