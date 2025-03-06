package com.screener.user_service_backend.service.impl;

import com.screener.user_service_backend.dto.request.UserRegisterRequestDTO;
import com.screener.user_service_backend.entity.User;
import com.screener.user_service_backend.exception.EmailOrUsernameAlreadyExistsException;
import com.screener.user_service_backend.mapper.UserMapper;
import com.screener.user_service_backend.repository.UserRepository;
import com.screener.user_service_backend.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserServiceImpl implements IUserService {

    private UserRepository userRepository;

    @Override
    public void createAuthUser(UserRegisterRequestDTO userRegisterRequestDTO) {
        User user = UserMapper.mapUserDtoToUser(userRegisterRequestDTO, new User());
        if(userRepository.findByEmail(userRegisterRequestDTO.getEmail()).isPresent()) {
            throw new EmailOrUsernameAlreadyExistsException("Email already exists");
        } else if (userRepository.findByUsername(userRegisterRequestDTO.getUsername()).isPresent()) {
            throw new EmailOrUsernameAlreadyExistsException("Username already exists");
        }
        userRepository.save(user);
    }
}
