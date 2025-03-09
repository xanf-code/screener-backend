package com.screener.utility_services.rabbit.dto;

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
