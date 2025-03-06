package com.screener.user_service_backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "User Request Body", description = "User request data transfer object")
public class UserRegisterRequestDTO {

    @NotEmpty(message = "Username cannot be empty")
    @Size(min = 3, max = 20)
    private String username;

    @NotEmpty(message = "First name cannot be empty")
    @Size(min = 3, max = 20)
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty")
    @Size(min = 3, max = 20)
    private String lastName;

    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 8, max = 20)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$", message = "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one number.")
    private String password;

    @NotEmpty(message = "Email cannot be empty")
    @Pattern(regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$", message = "Invalid email format")
    private String email;

    private List<String> preferredIndustries;
}
