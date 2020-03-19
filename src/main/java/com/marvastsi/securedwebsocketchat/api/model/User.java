package com.marvastsi.securedwebsocketchat.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.br.CPF;

import com.marvastsi.securedwebsocketchat.auth.UserAuth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USER", schema = "public")
public class User extends UserAuth {

	private static final long serialVersionUID = 1L;

	@CPF
	@NotNull
	@Column(name = "cpf", unique = true, updatable = false)
	private String cpf;

	private String name;

	protected String getUsernameAuth() {
		return this.getCpf();
	}
}
