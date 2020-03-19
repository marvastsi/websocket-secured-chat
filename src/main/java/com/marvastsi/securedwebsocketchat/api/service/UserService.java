package com.marvastsi.securedwebsocketchat.api.service;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.marvastsi.securedwebsocketchat.api.DTO.LoginDTO;
import com.marvastsi.securedwebsocketchat.api.model.Token;
import com.marvastsi.securedwebsocketchat.api.model.User;
import com.marvastsi.securedwebsocketchat.auth.JwtAuthenticator;
import com.marvastsi.securedwebsocketchat.auth.UnauthorizedException;
import com.marvastsi.securedwebsocketchat.auth.role.UserRole;
import com.marvastsi.securedwebsocketchat.repository.UserRepository;

@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder bcrypt;

	@Autowired
	private JwtAuthenticator jwtAuthenticator;

	public Optional<Token> authenticate(LoginDTO login) {
		String username = login.getLogin();
		Optional<User> optUser = userRepository.findByCpf(username);
		if (optUser.isEmpty()) {
			throw new EntityNotFoundException("Not found user: " + login.getLogin());
		}

		User user = optUser.get();
		if (bcrypt.matches(login.getPassword(), user.getPassword())) {
			return Optional.of(new Token(jwtAuthenticator.encode(user.getUsername(), null)));
		}

		throw new UnauthorizedException("Unauthorized login for username: " + login.getLogin());
	}

	public Optional<User> save(User user) {
		String cpf = removeMask(user.getCpf());
		user.setCpf(cpf);

		String pass = bcrypt.encode(user.getPassword());
		user.setPassword(pass);

		user.getRoles().add(UserRole.ROLE_API_USER.name());

		User savedUser = userRepository.save(user);
		if (savedUser != null) {
			return Optional.of(savedUser);
		}
		return Optional.empty();
	}

	public Optional<User> findByUsername(String username) {
		return userRepository.findByCpf(username);
	}

	private String removeMask(String value) {
		return (value != null) ? value.replaceAll("[^0-9]", "") : null;
	}
}
