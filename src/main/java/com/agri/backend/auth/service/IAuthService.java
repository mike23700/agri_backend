package com.agri.backend.auth.service;

import com.agri.backend.auth.dto.LoginRequestDto;
import com.agri.backend.auth.dto.RegisterRequestDto;
import com.agri.backend.user.entity.User;

public interface IAuthService {
    User registerUser(RegisterRequestDto registerRequest);
    User loginUser(LoginRequestDto request);
}