package com.dev.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.dev.DTO.JWTTokenResponseDTO;
import com.dev.DTO.JwtToken;
import com.dev.config.CustomUserDetailService;
import com.dev.entity.Users;
import com.dev.repo.UserRepository;
import com.dev.spring.helper.JwtUtil;


@Service
public class AuthService {
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomUserDetailService customUserDetailService;

	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserRepository userRepository;

	@SuppressWarnings("null")
	public JWTTokenResponseDTO generatToken(JwtToken jwtToken) throws Exception {
		
		if(Objects.isNull(jwtToken) && jwtToken.getUsername().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Username required.", null);
		}
		
		Users user = userRepository.findByEmail(jwtToken.getUsername());
		
		if (Objects.isNull(user) || user.getEmail().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found !!.", null);
		}

		JWTTokenResponseDTO jWTResponse = new JWTTokenResponseDTO();
		try {
			this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtToken.getUsername(), jwtToken.getPassword()));
		} catch (UsernameNotFoundException ex) {
			ex.printStackTrace();
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Bad Credential", null);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Bad Credential", null);
		}

		UserDetails userDetails = this.customUserDetailService.loadUserByUsername(jwtToken.getUsername());
		String generateToken = jwtUtil.generateToken(userDetails);
		jWTResponse.setToken(generateToken);
		jWTResponse.setToken_expiration(jwtUtil.extractExpiration(generateToken));
		return jWTResponse;
	}



}
