package org.silsagusi.joonggaemoa.global.auth.filter;

import java.io.IOException;

import org.silsagusi.joonggaemoa.domain.agent.controller.dto.LoginRequestDto;
import org.silsagusi.joonggaemoa.global.auth.jwt.JwtProvider;
import org.silsagusi.joonggaemoa.global.auth.jwt.RefreshToken;
import org.silsagusi.joonggaemoa.global.auth.userDetails.CustomUserDetails;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final JwtProvider jwtProvider;
	private final ObjectMapper objectMapper;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtProvider jwtProvider,
		ObjectMapper objectMapper) {
		this.authenticationManager = authenticationManager;
		this.jwtProvider = jwtProvider;
		this.objectMapper = objectMapper;
		setFilterProcessesUrl("/api/agent/login");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException {
		LoginRequestDto requestDto = null;
		try {
			requestDto = objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
			requestDto.getUsername(), requestDto.getPassword());

		try {
			return authenticationManager.authenticate(authToken);
		} catch (AuthenticationException e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		Authentication authResult) throws IOException, ServletException {

		CustomUserDetails customUserDetails = (CustomUserDetails)authResult;

		String accessToken = jwtProvider.generateAccessToken(authResult);
		String refreshToken = jwtProvider.generateRefreshToken(customUserDetails.getId());

		response.setStatus(HttpServletResponse.SC_OK);
		response.addHeader("Authorization", "Bearer " + accessToken);

		RefreshToken.putRefreshToken(customUserDetails.getId(), refreshToken);
	}
}
