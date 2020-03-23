package com.marvastsi.securedwebsocketchat.api.controller;

import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.marvastsi.securedwebsocketchat.annotation.RestConfig;
import com.marvastsi.securedwebsocketchat.annotation.RoleApiUser;
import com.marvastsi.securedwebsocketchat.api.model.Message;
import com.marvastsi.securedwebsocketchat.api.model.User;
import com.marvastsi.securedwebsocketchat.api.service.MessageService;
import com.marvastsi.securedwebsocketchat.api.service.UserService;
import com.marvastsi.securedwebsocketchat.socket.DTO.InputMessageDTO;
import com.marvastsi.securedwebsocketchat.socket.DTO.OutputMessageDTO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestConfig
@RequestMapping("/message")
@Api(tags = "Message")
public class MessageController {

	@Autowired
	private MessageService messageService;

	@Autowired
	private UserService userService;

	@Autowired
	private SimpMessageSendingOperations messagingTemplate;

	@RoleApiUser
	@PostMapping("/sendMessage/{toRecipient}")
	@ApiOperation("[SOCKET] Sent a new menssage.")
	public ResponseEntity<?> sendMessage(@PathVariable String toRecipient, @Valid @RequestBody InputMessageDTO mensagem,
			@ApiIgnore Errors err) {
		if (!err.hasErrors()) {
			
			User sender = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Optional<User> recipient = userService.findByUsername(toRecipient);
			
			Message msg = messageService.save(mensagem, sender, recipient.get());
			
			OutputMessageDTO out = new OutputMessageDTO(msg);
			messagingTemplate.convertAndSend(String.format("/channel/%s", toRecipient), out);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(out);
		}
		return ResponseEntity.badRequest()
				.body(
						err.getAllErrors()
						.stream()
						.map(msg -> msg.getDefaultMessage())
						.collect(Collectors.joining(",")));
	}

}
