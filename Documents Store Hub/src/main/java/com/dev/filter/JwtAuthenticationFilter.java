package com.dev.filter;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ResponseStatusException;

import com.dev.config.CustomUserDetailService;
import com.dev.spring.helper.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	private static final Logger _logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private CustomUserDetailService customUserDetailService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		_logger.info("JwtAuthenticationFilter called.");
		
		String tokenWithBearer = request.getHeader("Authorization");
		String extractUsername = "";

		if (Objects.nonNull(tokenWithBearer) && tokenWithBearer.startsWith("Bearer")) {
			_logger.info("authorization validation.");

			String tokenWithotBearer = tokenWithBearer.substring(7);

			try {
				extractUsername = jwtUtil.extractUsername(tokenWithotBearer);

			} catch (Exception e) {
				e.printStackTrace();
			}
			UserDetails userDetails = this.customUserDetailService.loadUserByUsername(extractUsername);

			if (Objects.nonNull(extractUsername) && SecurityContextHolder.getContext().getAuthentication() == null) {

				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());

				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

			} else {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "something went wrong");
			}
		}
		
		filterChain.doFilter(request, response);
	}
	
	public void validateJWUserNameEmail(String token) {
		Claims claims = Jwts.parser().setSigningKey("secret").parseClaimsJws(token).getBody();

		String iss = claims.get("iss", String.class);
		String username = claims.get("username", String.class);
		String email = claims.get("sub", String.class);
		System.out.println(iss + username + email);

	}

}
