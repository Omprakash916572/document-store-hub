package com.dev.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.DTO.UserResponseDTO;
import com.dev.DTO.UsersDTO;
import com.dev.service.UserService;

@RestController
@RequestMapping("/v1/user")
@CrossOrigin("*")
public class UserController {

	private static final Logger _logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;
	
	@PostMapping("/create-user")
	public UserResponseDTO createUser(@RequestBody UsersDTO usersDTO) {
		_logger.info("UserController called.");
		UserResponseDTO createUser = userService.createUser(usersDTO);
		return createUser;
	}

}
