package com.marvastsi.securedwebsocketchat.socket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

	@Override
	protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
		messages.anyMessage().authenticated();
	}

	@Override
	protected boolean sameOriginDisabled() {
		// Deixa a proteção das rotas, fazendo necessário aimplementação de bloqueio de
		// rotas alternativo (com filtro por exemplo)
		return true;
	}

}
