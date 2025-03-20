package org.silsagusi.joonggaemoa.global.auth.jwt.refreshToken.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenRequestDto {

	private String refreshToken;
}
