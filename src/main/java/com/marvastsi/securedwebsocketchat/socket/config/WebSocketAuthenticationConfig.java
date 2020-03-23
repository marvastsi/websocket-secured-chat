package com.marvastsi.securedwebsocketchat.socket.config;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.marvastsi.securedwebsocketchat.api.model.User;
import com.marvastsi.securedwebsocketchat.api.service.UserService;
import com.marvastsi.securedwebsocketchat.auth.JwtAuthenticator;

import io.jsonwebtoken.Claims;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketAuthenticationConfig implements WebSocketMessageBrokerConfigurer {

	private static final Logger logger = LoggerFactory.getLogger(WebSocketAuthenticationConfig.class);

	@Autowired
	private UserService userService;

	@Autowired
	private JwtAuthenticator jwtAuthenticator;

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(new ChannelInterceptor() {
			@Override
			public Message<?> preSend(Message<?> message, MessageChannel channel) {
				StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
				if (StompCommand.CONNECT.equals(accessor.getCommand())) {
					try {
						List<String> authorizations = accessor.getNativeHeader("X-Authorization");
						String bearer = authorizations.get(0);
						String split[] = bearer != null ? bearer.split(" ") : new String[0];
						String accessToken = split.length == 2 ? split[1] : null;
						Assert.notNull(accessToken, "Authorization must not be null");
						
						Optional<Claims> jwt = jwtAuthenticator.decode(Optional.of(accessToken));
						Authentication authentication = performAuthentication(jwt.get());
						accessor.setUser(authentication);
					} catch (Exception ex) {
						logger.error("Access danied for Authorization token. " + ex.getMessage(), ex);
					}
				}

				return message;
			}
		});
	}

	private Authentication performAuthentication(Claims claims) {
		if (checkExpiration(claims)) {
			Optional<User> optUser = userService.findByUsername(claims.getSubject());
			if (optUser.isPresent()) {
				User user = optUser.get();
				return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
			}
		}
		return null;
	}

	private boolean checkExpiration(Claims claims) {
		return claims.getExpiration() != null && new Date().before(claims.getExpiration());
	}
}
