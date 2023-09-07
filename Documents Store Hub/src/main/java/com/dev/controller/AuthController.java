package com.dev.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.DTO.JWTTokenResponseDTO;
import com.dev.DTO.JwtTokenDTO;
import com.dev.service.AuthService;

@RestController
@RequestMapping("/v1")
public class AuthController {
	
	private static final Logger _logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private AuthService authService;

	@PostMapping("/auth/token")
	public JWTTokenResponseDTO generateToken(@RequestBody JwtTokenDTO jwtToken) throws Exception {
		_logger.info("auth controller called.");
		return authService.generatToken(jwtToken);
	}

}
