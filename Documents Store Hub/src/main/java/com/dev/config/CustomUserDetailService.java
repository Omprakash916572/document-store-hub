package com.dev.config;

import java.util.ArrayList;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.dev.entity.Users;
import com.dev.repo.UserRepository;

@Service
public class CustomUserDetailService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Users user = userRepository.findByEmail(username);
		
		if (Objects.isNull(user) || user.getEmail().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found !!.", null);
		}

		if (user.getEmail().equals(username)) {
			return new User( user.getEmail(),user.getPassword(),new ArrayList<>());

		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user name not fund.");
		}
	}

}
