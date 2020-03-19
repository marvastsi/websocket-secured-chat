package com.marvastsi.securedwebsocketchat.auth.role;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {

	ROLE_API_USER;

	@Override
	public String getAuthority() {
		return name();
	}
}
