package com.screener.user_service_backend.messaging;

import com.screener.user_service_backend.dto.rabbit.EmailQueueDTO;
import com.screener.user_service_backend.entity.User;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserMessageProducer {

    private final RabbitTemplate rabbitTemplate;

    public UserMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendToEmailQueue(User user) {
        EmailQueueDTO emailQueueDTO = new EmailQueueDTO();
        emailQueueDTO.setEmail(user.getEmail());
        emailQueueDTO.setVerificationCode(user.getVerificationCode());

        rabbitTemplate.convertAndSend("user-email-service-queue", emailQueueDTO);
    }
}
