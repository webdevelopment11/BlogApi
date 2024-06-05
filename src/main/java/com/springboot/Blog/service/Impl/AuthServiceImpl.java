package com.springboot.Blog.service.Impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot.Blog.entity.Role;
import com.springboot.Blog.entity.User;
import com.springboot.Blog.exception.BlogApiException;
import com.springboot.Blog.payload.LoginDto;
import com.springboot.Blog.payload.RegisterDto;
import com.springboot.Blog.repository.RoleRepository;
import com.springboot.Blog.repository.UserRepository;
import com.springboot.Blog.security.JwtTokenProvider;
import com.springboot.Blog.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService{

//	private AuthenticationManager authenticationManager;
	private AuthenticationManager authenticationManager;
	
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private PasswordEncoder passwordEncoder;
	private JwtTokenProvider jwtTokenProvider;
	
	public AuthServiceImpl(AuthenticationManager authenticationManager,
			UserRepository userRepository,
			RoleRepository roleRepository,
			PasswordEncoder passwordEncoder,
			JwtTokenProvider jwtTokenProvider) {
		this.authenticationManager=authenticationManager;
		this.userRepository=userRepository;
		this.roleRepository=roleRepository;
		this.passwordEncoder=passwordEncoder;
		this.jwtTokenProvider=jwtTokenProvider;
		
	}
	
	
	@Override
	public String login(LoginDto loginDto) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				loginDto.getUsernameOrEmail(),loginDto.getPassword()));
		
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String token = jwtTokenProvider.generateToken(authentication);
		
		
		return token;
	}


	@Override
	public String register(RegisterDto registerDto) {
		if(userRepository.existsByUsername(registerDto.getUsername())) {
			throw new BlogApiException(HttpStatus.BAD_REQUEST, "User already exists");
		}
		
		if(userRepository.existsByEmail(registerDto.getEmail())) {
			throw new BlogApiException(HttpStatus.BAD_REQUEST, "Email is already exists");
			
		}
		
		User user = new User();
		user.setName(registerDto.getName());
		user.setEmail(registerDto.getEmail());
		user.setUsername(registerDto.getUsername());
		user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
		
		
		Set<Role> roles = new HashSet<>();
		Role userRole = roleRepository.findByName("ROLE_USER").get();
		roles.add(userRole);
		user.setRoles(roles);
		
		userRepository.save(user);
		
		return "USer registered SuccessFully";
		
	}

}
