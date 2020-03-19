package com.marvastsi.securedwebsocketchat.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UnauthorizedException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public UnauthorizedException(String message) {
		super(message);
	}
}