package com.marvastsi.securedwebsocketchat.api.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.marvastsi.securedwebsocketchat.api.model.User;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, String> {

	Optional<User> findByCpf(String cpf);
}
