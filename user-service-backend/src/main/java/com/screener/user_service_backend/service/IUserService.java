package com.screener.user_service_backend.service;

import com.screener.user_service_backend.dto.request.UserRegisterRequestDTO;

public interface IUserService {

    void createAuthUser(UserRegisterRequestDTO userRegisterRequestDTO);
}
