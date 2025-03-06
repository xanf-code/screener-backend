package com.screener.user_service_backend.constants;

public class UserConstants {

    private UserConstants() {}

    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_AUDIT = "ROLE_AUDIT";

    public static final String STATUS_201 = "201";
    public static final String STATUS_200 = "200";
    public static final String STATUS_400 = "400";
    public static final String STATUS_500 = "500";

    public static final String MESSAGE_201_USER_CREATED = "User created successfully";
    public static final String MESSAGE_200_USER_UPDATED = "User updated successfully";
    public static final String MESSAGE_200_RESEND_VERIFICATION_CODE = "Verification code resent successfully";
    public static final String MESSAGE_200_RESEND_VERIFICATION_EMAIL = "Password reset email sent";
    public static final String MESSAGE_200_ACCOUNT_VERIFIED = "Account verified successfully";
    public static final String MESSAGE_400_BAD_REQUEST = "Bad request";
    public static final String MESSAGE_500_INTERNAL_SERVER_ERROR = "Internal server error";
    public static final String MESSAGE_200_USER_FOUND = "User found successfully";
    public static final String MESSAGE_201_LOGGED_IN = "User authenticated and logged in successfully";
}
