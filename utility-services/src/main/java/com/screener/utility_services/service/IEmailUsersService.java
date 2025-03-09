package com.screener.utility_services.service;

import com.screener.utility_services.rabbit.dto.EmailQueueDTO;

public interface IEmailUsersService {

    void sendVerificationEmail(EmailQueueDTO user);
}
