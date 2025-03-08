package com.screener.user_service_backend.service.impl;

import com.screener.user_service_backend.dto.request.UserLoginRequestDTO;
import com.screener.user_service_backend.dto.request.UserRegisterRequestDTO;
import com.screener.user_service_backend.dto.request.VerifyUserDTO;
import com.screener.user_service_backend.entity.User;
import com.screener.user_service_backend.exception.EmailOrUsernameAlreadyExistsException;
import com.screener.user_service_backend.mapper.UserMapper;
import com.screener.user_service_backend.repository.UserRepository;
import com.screener.user_service_backend.service.IAuthenticationService;
import com.screener.user_service_backend.service.security.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements IAuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailServiceImpl emailServiceimpl;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    public void signup(UserRegisterRequestDTO registerUser){
        User user = UserMapper.mapUserDtoToUser(registerUser, new User());
        if(userRepository.findByEmail(registerUser.getEmail()).isPresent()) {
            throw new EmailOrUsernameAlreadyExistsException("Email already exists");
        } else if (userRepository.findByUsername(registerUser.getUsername()).isPresent()) {
            throw new EmailOrUsernameAlreadyExistsException("Username already exists");
        }
        user.setPassword(passwordEncoder.encode(registerUser.getPassword()));
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationExpiresAt(LocalDateTime.now().plusMinutes(15));
        user.setEnabled(false);
        sendVerificationEmail(user);
        userRepository.save(user);
    }

    @Override
    public User login(UserLoginRequestDTO loginUser) {
        User user = userRepository.findByUsername(loginUser.getUsername())
                .orElseThrow(() -> new EmailOrUsernameAlreadyExistsException("User not found"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Account not verified. Please verify your account.");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getUsername(),
                        loginUser.getPassword()
                )
        );

        return user;
    }

    @Override
    public void verifyUser(VerifyUserDTO input) {
        Optional<User> optionalUser = userRepository.findByEmail(input.getEmail());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getVerificationExpiresAt().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Verification code has expired");
            }
            if (user.getVerificationCode().equals(input.getVerificationCode())) {
                user.setEnabled(true);
                user.setAccountVerified(true);
                user.setVerificationCode(null);
                user.setVerificationExpiresAt(null);
                user.setUpdatedAt(LocalDateTime.now());
                userRepository.save(user);
            } else {
                throw new RuntimeException("Invalid verification code");
            }
        } else {
            throw new EmailOrUsernameAlreadyExistsException("User not found");
        }
    }

    @Override
        public void resendVerificationCode(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.isEnabled()) {
                throw new RuntimeException("Account is already verified");
            }
            user.setVerificationCode(generateVerificationCode());
            user.setVerificationExpiresAt(LocalDateTime.now().plusHours(1));
            sendVerificationEmail(user);
            userRepository.save(user);
        } else {
            throw new EmailOrUsernameAlreadyExistsException("User not found");
        }
    }

    @Override
    public void forgotPassword(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setResetPasswordToken(generatePasswordResetToken());
            user.setResetPasswordExpiresAt(LocalDateTime.now().plusMinutes(15));
            user.setUpdatedAt(LocalDateTime.now());
            // TODO: Send user reset password email
//            sendVerificationEmail(user);
            userRepository.save(user);
        }
    }

    @Override
    public User updateOldPasswordToNewPassword(String resetPasswordToken, String newPassword) {
        Optional<User> optionalUser = userRepository.findByResetPasswordToken(resetPasswordToken);
        if(optionalUser.isEmpty()) {
            throw new EmailOrUsernameAlreadyExistsException("User not found or invalid token");
        }
        User user = optionalUser.get();
        if(user.getResetPasswordExpiresAt().isBefore(LocalDateTime.now())) {
            // TODO: Add custom exception
            throw new RuntimeException("Password reset token has expired");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        user.setResetPasswordExpiresAt(null);
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public Boolean validateToken(String token) {
        try {
            return jwtService.isTokenValid(token);
        } catch (Exception e) {
            return false;
        }
    }

    private void sendVerificationEmail(User user) {
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
        }
    }

    private Integer generateVerificationCode() {
        Random random = new Random();
        return random.nextInt(900000) + 100000;
    }

    private String generatePasswordResetToken() {
        return UUID.randomUUID().toString();
    }
}
