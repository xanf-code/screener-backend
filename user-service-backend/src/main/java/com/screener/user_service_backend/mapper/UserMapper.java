package com.screener.user_service_backend.mapper;

import com.screener.user_service_backend.dto.request.UserRegisterRequestDTO;
import com.screener.user_service_backend.entity.User;

import java.time.LocalDateTime;

public class UserMapper {

    public static UserRegisterRequestDTO mapUserToUsersDto(User user, UserRegisterRequestDTO userRegisterRequestDTO) {
        userRegisterRequestDTO.setUsername(user.getUsername());
        userRegisterRequestDTO.setFirstName(user.getFirstName());
        userRegisterRequestDTO.setLastName(user.getLastName());
        userRegisterRequestDTO.setPassword(user.getPassword());
        userRegisterRequestDTO.setEmail(user.getEmail());
        userRegisterRequestDTO.setPreferredIndustries(user.getPreferredIndustries());
        return userRegisterRequestDTO;
    }

    public static User mapUserDtoToUser(UserRegisterRequestDTO userRegisterRequestDTO, User user) {
        user.setUsername(userRegisterRequestDTO.getUsername());
        user.setFirstName(userRegisterRequestDTO.getFirstName());
        user.setLastName(userRegisterRequestDTO.getLastName());
        user.setPassword(userRegisterRequestDTO.getPassword());
        user.setEmail(userRegisterRequestDTO.getEmail());
        user.setPreferredIndustries(userRegisterRequestDTO.getPreferredIndustries());
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }
}
