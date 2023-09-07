package com.dev.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.DTO.JWTTokenResponseDTO;
import com.dev.DTO.JwtToken;
import com.dev.service.AuthService;

@RestController
@RequestMapping("/v1")
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/auth/token")
	public JWTTokenResponseDTO generateToken(@RequestBody JwtToken jwtToken) throws Exception {
		return authService.generatToken(jwtToken);
	}

}
