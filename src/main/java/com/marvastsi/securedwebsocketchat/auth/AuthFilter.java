package com.marvastsi.securedwebsocketchat.auth;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.marvastsi.securedwebsocketchat.api.model.User;
import com.marvastsi.securedwebsocketchat.api.service.UserService;

import io.jsonwebtoken.Claims;

@Component
public class AuthFilter extends OncePerRequestFilter {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtAuthenticator jwtAuthenticator;

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws ServletException, IOException {

		String header = req.getHeader("X-Authorization");
		String split[] = header != null ? header.split(" ") : new String[0];
		String token = split.length == 2 ? split[1] : null;

		if (!StringUtils.isBlank(token)) {
			Optional<Claims> optClaims = jwtAuthenticator.decode(Optional.of(token));

			if (optClaims.isPresent()) {
				Claims claims = optClaims.get();
				performAuthentication(req, claims);
			}

		}
		chain.doFilter(req, res);
	}

	private boolean checkExpiration(Claims claims) {
		return claims.getExpiration() != null && new Date().before(claims.getExpiration());
	}

	private HttpServletRequest performAuthentication(HttpServletRequest request, Claims claims) {
		if (checkExpiration(claims)) {
			Optional<User> optUser = userService.findByUsername(claims.getSubject());
			if (optUser.isPresent()) {
				User user = optUser.get();
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null,
						user.getAuthorities());
				auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}

		return request;
	}

}
