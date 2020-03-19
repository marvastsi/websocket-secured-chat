package com.marvastsi.securedwebsocketchat.api.service;

import static java.lang.String.format;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.marvastsi.securedwebsocketchat.socket.model.ChatMessage;

@Service
@Transactional
public class MessageService {

	@Autowired
	private SimpMessageSendingOperations messagingTemplate;

	public void sendMessage(ChatMessage message) {
		messagingTemplate.convertAndSend(format("/channel/%s", message.getSender()), message);
		
		System.out.println(message);
	}

}