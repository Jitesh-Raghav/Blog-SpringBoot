package com.example.springbootblog.service;

import com.example.springbootblog.payload.LoginDto;
import com.example.springbootblog.payload.RegisterDto;

public interface AuthService {

    String Login(LoginDto loginDto);
    String Register(RegisterDto registerDto);
}
