package com.dev.service;

import java.util.Objects;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.dev.DTO.UserResponseDTO;
import com.dev.DTO.UsersDTO;
import com.dev.entity.Users;
import com.dev.repo.UserRepository;

@Service
public class UserService {

	private static final Logger _logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepository;

	public UserResponseDTO createUser(UsersDTO usersDTO) {
		_logger.info("create user function called.");

		UserResponseDTO userResponseDTO = null;

		if (Objects.nonNull(usersDTO)) {
			
			if (usersDTO.getEmail() != null && !usersDTO.getEmail().isEmpty()) {
				Users users = userRepository.findByEmail(usersDTO.getEmail());
				if (Objects.nonNull(users) && !users.getEmail().isEmpty()) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already present", null);
				}
			} else {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email required", null);
			}
			
			Users users = new Users();
			users = this.dtoToEntity(usersDTO, users);
			Users usersObjFromDB = userRepository.save(users);
			userResponseDTO = new UserResponseDTO();
			userResponseDTO = entityToDto(usersObjFromDB, userResponseDTO);
		}
		return userResponseDTO;

	}

	private UserResponseDTO entityToDto(Users sourceObject, UserResponseDTO targetObject) {

		if (sourceObject.getId() != null) {
			targetObject.setId(sourceObject.getId());
		}
		if (sourceObject.getUserName() != null && !sourceObject.getUserName().isEmpty()) {
			targetObject.setUsername(sourceObject.getUserName());
		}
		if (sourceObject.getEmail() != null && !sourceObject.getEmail().isEmpty()) {
			targetObject.setEmail(sourceObject.getEmail());
		}
		return targetObject;
	}

	private Users dtoToEntity(UsersDTO sourceObject, Users targetObject) {

		if (sourceObject.getFirstName() != null && !sourceObject.getFirstName().isEmpty()) {
			targetObject.setFirstName(sourceObject.getFirstName());
		}
		if (sourceObject.getLastName() != null && !sourceObject.getLastName().isEmpty()) {
			targetObject.setLastName(sourceObject.getLastName());
		}
		if (sourceObject.getLastName() != null && !sourceObject.getLastName().isEmpty()) {
			targetObject.setLastName(sourceObject.getLastName());
		}
		if (sourceObject.getPhoneNo() != null && !sourceObject.getPhoneNo().isEmpty()) {
			targetObject.setPhoneNo(sourceObject.getPhoneNo());
		}
		if (sourceObject.getEmail() != null && !sourceObject.getEmail().isEmpty()) {
			Pattern emailValidation = this.emailValidation();

			if (emailValidation.matcher(sourceObject.getEmail()).matches()) {
				targetObject.setEmail(sourceObject.getEmail());
			} else {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Email not valid : " + sourceObject.getEmail(), null);
			}
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email required", null);
		}

		if (sourceObject.getUsername() != null && !sourceObject.getUsername().isEmpty()) {
			targetObject.setUserName(sourceObject.getUsername());
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User name required", null);
		}

		if (sourceObject.getPassword() != null && !sourceObject.getPassword().isEmpty()) {
			targetObject.setPassword(sourceObject.getPassword());
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "password name required", null);
		}
		return targetObject;
	}

	public Pattern emailValidation() {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";
		Pattern emailPattern = Pattern.compile(emailRegex);
		return emailPattern;
	}

}
