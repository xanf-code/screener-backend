package com.screener.user_service_backend.controller;
import com.screener.user_service_backend.constants.UserConstants;
import com.screener.user_service_backend.dto.request.*;
import com.screener.user_service_backend.dto.response.GenericResponseDTO;
import com.screener.user_service_backend.dto.response.UpdatePasswordResponseDTO;
import com.screener.user_service_backend.dto.response.UserLoginResponseDTO;
import com.screener.user_service_backend.dto.response.UserRegisterResponseDTO;
import com.screener.user_service_backend.entity.User;
import com.screener.user_service_backend.service.impl.AuthenticationServiceImpl;
import com.screener.user_service_backend.service.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Authentication", description = "The Authentication API")
@RestController
@RequestMapping(value = "/api/v1/auth", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
public class UserAuthController {

    private final JwtService jwtService;
    private final AuthenticationServiceImpl authenticationService;

    @Operation(summary = "Create a new user", description = "Create a new user", method = "POST", tags = {"Authentication"})
    @ApiResponse(responseCode = "200", description = "User created successfully")
    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponseDTO> createAuthUser(@Valid @RequestBody UserRegisterRequestDTO userRegisterRequestDTO) {
        authenticationService.signup(userRegisterRequestDTO);
        UserRegisterResponseDTO createdUserResponse = UserRegisterResponseDTO.builder()
                .username(userRegisterRequestDTO.getUsername())
                .firstName(userRegisterRequestDTO.getFirstName())
                .lastName(userRegisterRequestDTO.getLastName())
                .statusCode(UserConstants.STATUS_200)
                .statusMessage(UserConstants.MESSAGE_201_USER_CREATED)
                .email(userRegisterRequestDTO.getEmail())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createdUserResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> authenticate(@Valid @RequestBody UserLoginRequestDTO loginUserDto, HttpServletResponse response){
        User authenticatedUser = authenticationService.login(loginUserDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);

        // TODO: If this does not automatically set the cookie, we can remove this and set it in the frontend
        Cookie jwtCookie = new Cookie("jwt", jwtToken);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(24 * 60 * 60);
        response.addCookie(jwtCookie);

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

    @PostMapping("/verify-user")
    public ResponseEntity<GenericResponseDTO> verifyUser(@Valid @RequestBody VerifyUserDTO verifyUserDto) {
        authenticationService.verifyUser(verifyUserDto);
        GenericResponseDTO genericVerifyUserResponseDTO = GenericResponseDTO.builder()
                .message(UserConstants.MESSAGE_200_ACCOUNT_VERIFIED)
                .status(UserConstants.STATUS_200)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(genericVerifyUserResponseDTO);
    }

    @PostMapping("/resend-code")
    public ResponseEntity<GenericResponseDTO> resendVerificationCode(@Valid @RequestBody ResetVerificationCodeRequestDTO resetVerificationCodeRequestDTO) {
        authenticationService.resendVerificationCode(resetVerificationCodeRequestDTO.getEmail());
        GenericResponseDTO genericResendCodeResponseDTO = GenericResponseDTO.builder()
                .message(UserConstants.MESSAGE_200_RESEND_VERIFICATION_CODE)
                .status(UserConstants.STATUS_200)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(genericResendCodeResponseDTO);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<GenericResponseDTO> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDTO forgotPasswordRequestDTO) {
        // TODO: See what can be done hger? maybe have a separate DTO instead of handling here
        if(forgotPasswordRequestDTO.getEmail() == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        authenticationService.forgotPassword(forgotPasswordRequestDTO.getEmail());
        GenericResponseDTO genericResendEmailResponseDTO = GenericResponseDTO.builder()
                .message(UserConstants.MESSAGE_200_RESEND_VERIFICATION_EMAIL)
                .status(UserConstants.STATUS_200)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(genericResendEmailResponseDTO);
    }

    @PostMapping("/update-password")
    public ResponseEntity<UpdatePasswordResponseDTO> updateOldPasswordToNewPassword(@Valid @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        User user = authenticationService.updateOldPasswordToNewPassword(updatePasswordDTO.getToken(), updatePasswordDTO.getNewPassword());
        UpdatePasswordResponseDTO updatePasswordResponseDTO = UpdatePasswordResponseDTO.builder()
                .userName(user.getUsername())
                .message(UserConstants.MESSAGE_200_USER_UPDATED)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(updatePasswordResponseDTO);
    }

    @PostMapping("/validate-token")
    public ResponseEntity<GenericResponseDTO> validateToken(@NotEmpty(message = "Token cannot be empty") @RequestParam String token) {
        log.info("Starting to validate token");
        boolean isValid = authenticationService.validateToken(token);
        log.info("Token validity: {}", isValid);
        GenericResponseDTO genericResendEmailResponseDTO = GenericResponseDTO.builder()
                .message(isValid ? UserConstants.MESSAGE_200_VALID_TOKEN : UserConstants.MESSAGE_400_INVALID_TOKEN)
                .status(isValid ? UserConstants.STATUS_200 : UserConstants.STATUS_400)
                .build();
        return ResponseEntity.status(isValid ? HttpStatus.OK : HttpStatus.BAD_REQUEST).body(genericResendEmailResponseDTO);
    }
}
