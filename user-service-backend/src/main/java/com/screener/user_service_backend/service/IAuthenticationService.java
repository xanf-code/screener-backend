package com.screener.user_service_backend.service;

import com.screener.user_service_backend.dto.request.UserLoginRequestDTO;
import com.screener.user_service_backend.dto.request.UserRegisterRequestDTO;
import com.screener.user_service_backend.dto.request.VerifyUserDTO;
import com.screener.user_service_backend.entity.User;

import java.util.Optional;

public interface IAuthenticationService {

    void signup(UserRegisterRequestDTO user);

    User login(UserLoginRequestDTO user);

    void verifyUser(VerifyUserDTO input);

    void resendVerificationCode(String email);

    void forgotPassword(String email);

    User updateOldPasswordToNewPassword(String resetPasswordToken, String newPassword);
}
