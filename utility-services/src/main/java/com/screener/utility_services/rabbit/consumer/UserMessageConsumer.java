package com.screener.utility_services.rabbit.consumer;

import com.screener.utility_services.rabbit.dto.EmailQueueDTO;
import com.screener.utility_services.service.IEmailUsersService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class UserMessageConsumer {

    private final IEmailUsersService emailUsersService;

    public UserMessageConsumer(IEmailUsersService emailUsersService) {
        this.emailUsersService = emailUsersService;
    }

    @RabbitListener(queues = "user-email-service-queue")
    public void receiveMessage(EmailQueueDTO emailQueueDTO) {
        emailUsersService.sendVerificationEmail(emailQueueDTO);
    }
}
