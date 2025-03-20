package org.silsagusi.joonggaemoa.global.auth.jwt.refreshToken.controller;

import org.silsagusi.joonggaemoa.global.api.ApiResponse;
import org.silsagusi.joonggaemoa.global.auth.jwt.refreshToken.service.RefreshTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RefreshTokenController {

	private final RefreshTokenService refreshTokenService;

	@PostMapping("/api/token-refresh")
	public ResponseEntity<ApiResponse<?>> tokenRefresh(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
		var result = refreshTokenService.refreshToken(refreshTokenRequestDto.getRefreshToken());

		return ResponseEntity.ok(ApiResponse.ok(result));
	}
}
