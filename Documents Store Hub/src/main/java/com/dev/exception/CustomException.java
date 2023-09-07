package com.dev.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class CustomException {

	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<ApiResponse> runTimeException(ResponseStatusException ex) {
		ApiResponse apiResponse = new ApiResponse();
		
		apiResponse.setMessage(ex.getReason());
        apiResponse.setStatusCode(ex.getRawStatusCode());
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
