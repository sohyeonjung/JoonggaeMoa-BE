package org.silsagusi.joonggaemoa.global.auth.jwt.refreshToken;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.silsagusi.joonggaemoa.global.api.exception.CustomException;
import org.silsagusi.joonggaemoa.global.api.exception.ErrorCode;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RefreshToken {

	protected static final Map<String, Long> refreshTokens = new HashMap<>();

	public static Long getRefreshToken(String refreshToken) {
		return Optional.ofNullable(refreshTokens.get(refreshToken))
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));
	}

	public static void putRefreshToken(String refreshToken, Long id) {
		refreshTokens.put(refreshToken, id);
	}

	public static void removeRefreshToken(String refreshToken) {
		refreshTokens.remove(refreshToken);
	}

	public static void removeUserRefreshToken(String refreshToken) {
		for (Map.Entry<String, Long> entry : refreshTokens.entrySet()) {
			if (entry.getValue().equals(refreshToken)) {
				refreshTokens.remove(entry.getKey());
			}
		}
	}
}
