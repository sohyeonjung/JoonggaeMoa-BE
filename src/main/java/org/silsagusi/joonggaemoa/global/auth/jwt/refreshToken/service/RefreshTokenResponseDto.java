package org.silsagusi.joonggaemoa.global.auth.jwt.refreshToken.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenResponseDto {

	private String accessToken;
	private String refreshToken;
}
