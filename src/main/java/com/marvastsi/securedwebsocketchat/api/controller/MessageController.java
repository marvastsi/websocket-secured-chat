package com.marvastsi.securedwebsocketchat.api.controller;

import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.marvastsi.securedwebsocketchat.annotation.RestConfig;
import com.marvastsi.securedwebsocketchat.annotation.RoleApiUser;
import com.marvastsi.securedwebsocketchat.api.service.MessageService;
import com.marvastsi.securedwebsocketchat.socket.model.ChatMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestConfig
@RequestMapping("/message")
@Api(tags = "Message")
public class MessageController {

	@Autowired
	private MessageService messageService;
	
	@RoleApiUser
	@PostMapping("/sendMessage")
	@ApiOperation("[SOCKET] Sent a new menssage.")
	public ResponseEntity<?> sendMessage(@Valid @RequestBody ChatMessage mensagem, @ApiIgnore Errors err) {
		if (!err.hasErrors()) {
			messageService.sendMessage(mensagem);
			return ResponseEntity.status(HttpStatus.OK).body("Message sent.");
		}
		return ResponseEntity
				.badRequest()
				.body(err
						.getAllErrors()
						.stream()
						.map(msg -> msg.getDefaultMessage())
						.collect(Collectors.joining(",")));
	}
	
}
