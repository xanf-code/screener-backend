package com.screener.user_service_backend.controller;
import com.screener.user_service_backend.constants.UserConstants;
import com.screener.user_service_backend.dto.request.UpdatePasswordDTO;
import com.screener.user_service_backend.dto.request.UserLoginRequestDTO;
import com.screener.user_service_backend.dto.request.UserRegisterRequestDTO;
import com.screener.user_service_backend.dto.request.VerifyUserDTO;
import com.screener.user_service_backend.dto.response.UpdatePasswordResponseDTO;
import com.screener.user_service_backend.dto.response.UserLoginResponseDTO;
import com.screener.user_service_backend.dto.response.UserRegisterResponseDTO;
import com.screener.user_service_backend.entity.User;
import com.screener.user_service_backend.service.impl.AuthenticationServiceImpl;
import com.screener.user_service_backend.service.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Tag(name = "Authentication", description = "The Authentication API")
@RestController
@RequestMapping(value = "/api/v1/auth", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class UserAuthController {

    private final JwtService jwtService;
    private final AuthenticationServiceImpl authenticationService;

    @Operation(summary = "Create a new user", description = "Create a new user", method = "POST", tags = {"Authentication"})
    @ApiResponse(responseCode = "200", description = "User created successfully")
    @PostMapping(value = "/register")
    public ResponseEntity<UserRegisterResponseDTO> createAuthUser(@Valid @RequestBody UserRegisterRequestDTO userRegisterRequestDTO) {
        authenticationService.signup(userRegisterRequestDTO);
        UserRegisterResponseDTO createdUserResponse = UserRegisterResponseDTO.builder()
                .username(userRegisterRequestDTO.getUsername())
                .firstName(userRegisterRequestDTO.getFirstName())
                .lastName(userRegisterRequestDTO.getLastName())
                .statusCode(UserConstants.STATUS_200)
                .statusMessage(UserConstants.MESSAGE_201)
                .email(userRegisterRequestDTO.getEmail())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdUserResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> authenticate(@RequestBody UserLoginRequestDTO loginUserDto){
        User authenticatedUser = authenticationService.login(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);
        UserLoginResponseDTO authenticatedUserResponse = UserLoginResponseDTO.builder()
                .username(loginUserDto.getUsername())
                .statusCode(UserConstants.STATUS_200)
                .statusMessage(UserConstants.MESSAGE_201_LOGGED_IN)
                .jwtToken(jwtToken)
                .expirationTime(jwtService.getExpirationTime())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authenticatedUserResponse);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerifyUserDTO verifyUserDto) {
        try {
            authenticationService.verifyUser(verifyUserDto);
            // TODO: Have a success message with verification DTO
            return ResponseEntity.ok("Account verified successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email) {
        try {
            authenticationService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code sent");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email) {
        authenticationService.resetPassword(email);
        return ResponseEntity.ok("Password reset email sent");
    }

    @PostMapping("/update-password")
    public ResponseEntity<?> updateOldPasswordToNewPassword(@Valid @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        User user = authenticationService.updateOldPasswordToNewPassword(updatePasswordDTO.getToken(), updatePasswordDTO.getNewPassword());
        UpdatePasswordResponseDTO updatePasswordResponseDTO = UpdatePasswordResponseDTO.builder()
                .userName(user.getUsername())
                .message(UserConstants.MESSAGE_200)
                .build();
        return ResponseEntity.ok(updatePasswordResponseDTO);
    }
}
