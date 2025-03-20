package org.silsagusi.joonggaemoa.global.api.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	UNAUTHORIZED(401, HttpStatus.UNAUTHORIZED, "토큰이 없습니다."),
	FORBIDDEN(403, HttpStatus.FORBIDDEN, "권한이 없습니다."),
	NOT_FOUND_ELEMENT(404, HttpStatus.NOT_FOUND, "존재하지 않는 엔티티입니다."),
	NOT_FOUND_END_POINT(404, HttpStatus.NOT_FOUND, "존재하지 않는 API입니다."),
	INTERNAL_SERVER_ERROR(500, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.");

	private final Integer code;
	private final HttpStatus httpStatus;
	private final String message;
}
