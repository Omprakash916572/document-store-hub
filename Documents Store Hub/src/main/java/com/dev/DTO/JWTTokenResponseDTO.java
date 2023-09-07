package com.dev.DTO;

import java.util.Date;

import javax.xml.crypto.Data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class JWTTokenResponseDTO {

	private String token;
	private Date token_expiration;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getToken_expiration() {
		return token_expiration;
	}

	public void setToken_expiration(Date token_expiration) {
		this.token_expiration = token_expiration;
	}

}
