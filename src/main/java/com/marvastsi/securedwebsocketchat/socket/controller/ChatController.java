package com.marvastsi.securedwebsocketchat.socket.controller;

import static java.lang.String.format;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import com.marvastsi.securedwebsocketchat.socket.model.ChatMessage;
import com.marvastsi.securedwebsocketchat.socket.model.ChatMessage.MessageType;

@Controller
public class ChatController {
	
	@Autowired
	private SimpMessageSendingOperations messagingTemplate;

	/**
	 * Send broadcast message.
	 * @param chatMessage
	 * @return
	 */
	@MessageMapping("/chat.sendMessage")
	@SendTo("/channel/public")
	public ChatMessage sendBroadcastMessage(@Payload ChatMessage chatMessage) {
		System.out.println("chat.sendMessage: " + chatMessage);
		return chatMessage;
	}

	/**
	 * Sends a message to the specified room
	 * @param roomId
	 * @param chatMessage
	 * @return
	 */
	@MessageMapping("/chat.sendMessage/{roomId}")
	@SendTo("/channel/{roomId}")
	public ChatMessage sendMessageToRoom(@DestinationVariable String roomId, @Payload ChatMessage chatMessage) {
		System.out.println("/chat.sendMessage/{roomId}: " + chatMessage);
		return chatMessage;
	}
	
	/**
	 * Used to join an user without authentication
	 * @param roomId
	 * @param chatMessage
	 * @param headerAccessor
	 * @return
	 */
	@MessageMapping("/chat.addUser/{roomId}")
	@SendTo("/channel/{roomId}")
	public ChatMessage addUser(@DestinationVariable String roomId, @Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
		String currentRoomId = (String) headerAccessor.getSessionAttributes().put("room_id", roomId);
		if (currentRoomId != null) {
			ChatMessage leaveMessage = new ChatMessage();
			leaveMessage.setType(MessageType.LEAVE);
			leaveMessage.setSender(chatMessage.getSender());
			messagingTemplate.convertAndSend(format("/channel/%s", currentRoomId), leaveMessage);
			System.out.println("/chat.sendMessage/{roomId} LEAVE: " + leaveMessage);
		}
		headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
		System.out.println("/chat.sendMessage/{roomId}: " + chatMessage);
		return chatMessage;
	}

}
