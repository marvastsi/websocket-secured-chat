package com.marvastsi.securedwebsocketchat.socket.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import com.marvastsi.securedwebsocketchat.api.model.Message;
import com.marvastsi.securedwebsocketchat.api.model.User;
import com.marvastsi.securedwebsocketchat.api.service.MessageService;
import com.marvastsi.securedwebsocketchat.api.service.UserService;
import com.marvastsi.securedwebsocketchat.socket.DTO.InputMessageDTO;
import com.marvastsi.securedwebsocketchat.socket.DTO.OutputMessageDTO;

@Controller
public class ChatController {

	private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

	@Autowired
	private MessageService messageService;

	@Autowired
	private UserService userService;

	/**
	 * Send broadcast message.
	 * 
	 * @param inputMessage
	 * @return
	 */
	@MessageMapping("/chat.sendMessage")
	@SendTo("/channel/public")
	public OutputMessageDTO sendBroadcastMessage(@Payload InputMessageDTO inputMessage,
			SimpMessageHeaderAccessor headerAccessor) {

		User sender = (User) ((Authentication) headerAccessor.getUser()).getPrincipal();
		Message msg = messageService.save(inputMessage, sender, null);

		logger.debug("chat.sendMessage: " + inputMessage);
		return new OutputMessageDTO(msg);
	}

	/**
	 * Sends a message to the specified room
	 * 
	 * @param roomId
	 * @param inputMessage
	 * @return
	 */
	@MessageMapping("/chat.sendMessage/{roomId}")
	@SendTo("/channel/{roomId}")
	public OutputMessageDTO sendMessageToRoom(@DestinationVariable String roomId, @Payload InputMessageDTO inputMessage,
			SimpMessageHeaderAccessor headerAccessor) {

		Optional<User> optUser = userService.findByUsername(roomId);
		User sender = (User) ((Authentication) headerAccessor.getUser()).getPrincipal();
		Message msg = messageService.save(inputMessage, sender, optUser.get());

		logger.debug("/chat.sendMessage/{roomId}: " + msg);
		return new OutputMessageDTO(msg);
	}

}
