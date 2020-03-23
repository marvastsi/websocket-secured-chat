package com.marvastsi.securedwebsocketchat.api.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.marvastsi.securedwebsocketchat.api.model.Message;
import com.marvastsi.securedwebsocketchat.api.model.User;
import com.marvastsi.securedwebsocketchat.api.repository.MessageRepository;
import com.marvastsi.securedwebsocketchat.socket.DTO.InputMessageDTO;

@Service
@Transactional
public class MessageService {

	@Autowired
	private MessageRepository messageRepository;

	public Message save(InputMessageDTO dto, User sender, User recipient) {
		Message msg = new Message();
		msg.setContent(dto.getContent());
		msg.setType(dto.getType());
		msg.setSender(sender);
		msg.setRecipient(recipient);
		return messageRepository.save(msg);
	}

}