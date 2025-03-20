package org.silsagusi.joonggaemoa.global.auth.jwt.refreshToken.service;

import org.silsagusi.joonggaemoa.domain.agent.entity.Agent;
import org.silsagusi.joonggaemoa.domain.agent.repository.AgentRepository;
import org.silsagusi.joonggaemoa.global.api.exception.CustomException;
import org.silsagusi.joonggaemoa.global.api.exception.ErrorCode;
import org.silsagusi.joonggaemoa.global.auth.jwt.JwtProvider;
import org.silsagusi.joonggaemoa.global.auth.jwt.refreshToken.RefreshToken;
import org.silsagusi.joonggaemoa.global.auth.userDetails.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

	private final JwtProvider jwtProvider;
	private final AgentRepository agentRepository;

	public RefreshTokenResponseDto refreshToken(String refreshToken) {
		if (jwtProvider.validateToken(refreshToken)) {
			throw new CustomException(ErrorCode.UNAUTHORIZED);
		}

		Long id = RefreshToken.getRefreshToken(refreshToken);
		Agent agent = agentRepository.findById(id)
			.orElseThrow(() -> new CustomException(ErrorCode.UNAUTHORIZED));

		Authentication authentication = (Authentication)CustomUserDetails.create(agent);

		String newAccessToken = jwtProvider.generateAccessToken(authentication);
		String newRefreshToken = jwtProvider.generateRefreshToken(agent.getUsername());

		RefreshToken.removeRefreshToken(refreshToken);

		return RefreshTokenResponseDto.builder()
			.accessToken(newAccessToken)
			.refreshToken(newRefreshToken)
			.build();
	}

}
