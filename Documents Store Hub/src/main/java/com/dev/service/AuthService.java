package com.dev.service;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.dev.DTO.JWTTokenResponseDTO;
import com.dev.DTO.JwtTokenDTO;
import com.dev.config.CustomUserDetailService;
import com.dev.entity.Users;
import com.dev.repo.UserRepository;
import com.dev.spring.helper.JwtUtil;


@Service
public class AuthService {
	
	private static final Logger _logger = LoggerFactory.getLogger(AuthService.class);
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomUserDetailService customUserDetailService;

	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserRepository userRepository;

	@SuppressWarnings("null")
	public JWTTokenResponseDTO generatToken(JwtTokenDTO jwtTokenDTO) throws Exception {
		
		_logger.info("token generate api called.");
		
		if(Objects.isNull(jwtTokenDTO) && jwtTokenDTO.getUsername().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Username required.", null);
		}
		
		Users user = userRepository.findByEmail(jwtTokenDTO.getUsername());
		
		if (Objects.isNull(user) || user.getEmail().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found !!.", null);
		}

		try {
			this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtTokenDTO.getUsername(), jwtTokenDTO.getPassword()));
		} catch (UsernameNotFoundException ex) {
			ex.printStackTrace();
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Bad Credential", null);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Bad Credential", null);
		}

		UserDetails userDetails = this.customUserDetailService.loadUserByUsername(jwtTokenDTO.getUsername());
		String jwtToken = jwtUtil.generateToken(userDetails);
		JWTTokenResponseDTO jWTResponse = new JWTTokenResponseDTO();
		if (jwtToken != null && !jwtToken.isEmpty()) {
			_logger.info("token generated succefully.");
			jWTResponse.setToken(jwtToken);
			jWTResponse.setToken_expiration(jwtUtil.extractExpiration(jwtToken));
		}else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Something went wrong to generate token.", null);
		}
		return jWTResponse;
	}



}
