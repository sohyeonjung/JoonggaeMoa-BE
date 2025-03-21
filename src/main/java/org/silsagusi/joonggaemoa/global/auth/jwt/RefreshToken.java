package org.silsagusi.joonggaemoa.global.auth.jwt;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.silsagusi.joonggaemoa.global.api.exception.CustomException;
import org.silsagusi.joonggaemoa.global.api.exception.ErrorCode;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RefreshToken {

	protected static final Map<Long, String> refreshTokens = new HashMap<>();

	public static String getRefreshToken(Long id) {
		return Optional.ofNullable(refreshTokens.get(id))
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));
	}

	public static void putRefreshToken(Long id, String refreshToken) {
		refreshTokens.put(id, refreshToken);
	}

	public static void removeRefreshToken(Long id) {
		refreshTokens.remove(id);
	}

	public static void removeUserFromRefreshToken(String refreshToken) {
		for (Map.Entry<Long, String> entry : refreshTokens.entrySet()) {
			if (entry.getValue().equals(refreshToken)) {
				refreshTokens.remove(entry.getKey());
			}
		}
	}
}
