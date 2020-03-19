package com.marvastsi.securedwebsocketchat.auth;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtAuthenticator {

	@Value("${app.security.secret}")
	private String SECRET;
	
	enum TokenType {
		AUTHENTICATION;
		public static TokenType fromString(String name) {
			for (TokenType value : TokenType.values()) {
				if (value.name().equalsIgnoreCase(name)) {
					return value;
				}
			}
			return null;
		}
	}

	/**
	 * Generates a new authentication token (JWT) within the given
	 * {@literal subject} and {@literal payload}.
	 * 
	 * @param payload A Map object containing information options that can be used
	 *                during or after token validation.
	 * 
	 *                <pre>
	 * {
	 *   "login": "guest@server.com",
	 *   "applicationName": "MY_APP",
	 *   "Roles": "[Roles, To ,Authorize]",
	 *   "api_version": "v1.0.0" 
	 * }
	 *                </pre>
	 * 
	 * @return A String containing a JWT formatted token.
	 */
	public String encode(String subject, Map<String, Object> payload) {
//		String typeSubject = TokenType.AUTHENTICATION.toString();
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, +1);
		Map<String, Object> claims = payload != null ? new HashMap<>(payload) : new HashMap<>();
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(date)
				.setExpiration(calendar.getTime())
				.signWith(SignatureAlgorithm.HS512, SECRET.getBytes())
				.compact();
	}

	public Optional<Claims> decode(Optional<String> token) {
		try {
			return Optional.of(
					Jwts.parser()
					 .setSigningKey(SECRET.getBytes())
					 .parseClaimsJws(token.get())
					 .getBody());
		} catch (Exception e) {
			return Optional.empty();
		}
	}

}
