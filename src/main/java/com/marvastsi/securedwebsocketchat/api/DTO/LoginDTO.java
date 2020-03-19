package com.marvastsi.securedwebsocketchat.api.DTO;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import lombok.Data;

@Data
public class LoginDTO {

	@JsonPropertyDescription("login")
	@CPF
	private String login;
	@NotBlank
	private String password;
}
