package com.screener.utility_services.service;

import com.screener.utility_services.rabbit.dto.EmailQueueDTO;
import com.screener.utility_services.service.impl.EmailServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailUsersService implements IEmailUsersService {

    private final EmailServiceImpl emailServiceimpl;

    @Override
    public void sendVerificationEmail(EmailQueueDTO user) {
        System.out.println(user.getEmail());
        System.out.println(user.getVerificationCode());
        String subject = "Account Verification";
        String verificationCode = "VERIFICATION CODE " + user.getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailServiceimpl.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            // TODO: Handle email sending exception
            e.printStackTrace();
        } catch (jakarta.mail.MessagingException e) {
            // TODO: Handle email sending exception
            e.printStackTrace();
        }
    }
}
