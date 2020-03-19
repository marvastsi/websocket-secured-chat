package com.marvastsi.securedwebsocketchat.socket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketBrokerConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();
		registry.addEndpoint("/ws").withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.setApplicationDestinationPrefixes("/app");
		// Enables a simple in-memory broker
		registry.enableSimpleBroker("/channel");

		// Use this for enabling a full featured broker like RabbitMQ or Redis
		/*
		 * registry.enableStompBrokerRelay("/topic")
		 * 		.setRelayHost("localhost")
		 * 		.setRelayPort(61613)
		 * 		.setClientLogin("user")
		 * 		.setClientPasscode("pass");
		 */
	}
}
