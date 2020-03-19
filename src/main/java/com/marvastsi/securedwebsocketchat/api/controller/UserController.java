package com.marvastsi.securedwebsocketchat.api.controller;

import java.sql.SQLException;
import java.util.Optional;
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
import com.marvastsi.securedwebsocketchat.api.DTO.LoginDTO;
import com.marvastsi.securedwebsocketchat.api.model.Token;
import com.marvastsi.securedwebsocketchat.api.model.User;
import com.marvastsi.securedwebsocketchat.api.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestConfig
@RequestMapping("/user")
@Api(tags = "User")
public class UserController {

	@Autowired
	private UserService userService;
	
	@PostMapping("/authenticate")
	@ApiOperation("Authenticate an user.")
	public ResponseEntity<?> authenticate(@Valid @RequestBody LoginDTO loginDTO, @ApiIgnore Errors err) {
		if (!err.hasErrors()) {
			Optional<Token> token = userService.authenticate(loginDTO);
			return ResponseEntity.status(HttpStatus.OK).body(token.get());
		}
		return ResponseEntity
				.badRequest()
				.body(err
						.getAllErrors()
						.stream()
						.map(msg -> msg.getDefaultMessage())
						.collect(Collectors.joining(",")));
	}
	
	@PostMapping
	@ApiOperation("Creates a new User.")
	public ResponseEntity<?> save(@Valid @RequestBody User user, @ApiIgnore Errors err) throws SQLException {
		if (!err.hasErrors()) {
			Optional<User> optUser = userService.save(user);
			if (optUser.isPresent()) {
				return ResponseEntity.status(HttpStatus.OK).body(optUser.get());				
			}
			throw new SQLException("Can't complete operation because an internal error.");
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
