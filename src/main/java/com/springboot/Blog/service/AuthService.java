package com.springboot.Blog.service;

import com.springboot.Blog.payload.LoginDto;
import com.springboot.Blog.payload.RegisterDto;

public interface AuthService {
	String login(LoginDto loginDto);
	
	String register(RegisterDto registerDto);

}
