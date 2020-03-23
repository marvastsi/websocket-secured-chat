package com.marvastsi.securedwebsocketchat.socket.controller;

import java.security.Principal;
import java.util.Optional;

//import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.marvastsi.securedwebsocketchat.api.model.Message;
import com.marvastsi.securedwebsocketchat.api.model.User;
import com.marvastsi.securedwebsocketchat.api.service.UserService;

@Component
public class WebSocketEventListener {

	private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

	@Autowired
	private UserService userService;

	@Autowired
	private SimpMessageSendingOperations messagingTemplate;

	@EventListener
	public void handleWebSocketConnectListener(SessionConnectedEvent event) {
		Principal user = event.getUser();
		logger.info("Received a new socket connection for user: " + user != null ? user.getName() : null);
	}

	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		Principal user = headerAccessor.getUser();

		if (user != null) {
			String username = user.getName();
			try {
				logger.debug("Usuário desconectado: " + username);
				Optional<User> optUser = userService.findByUsername(username);

				Message chatMessage = new Message();
				chatMessage.setType(Message.MessageType.LEAVE);
				chatMessage.setSender(optUser.get());
				chatMessage.setRecipient(optUser.get());

				messagingTemplate.convertAndSend(String.format("/channel/%s", username), chatMessage);
				messagingTemplate.convertAndSend("/channel/public", chatMessage);
			} catch (Exception e) {
				logger.error("Erro ao desconectar o usuário: " + username + " da sala: " + username + ".\n"
						+ e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
