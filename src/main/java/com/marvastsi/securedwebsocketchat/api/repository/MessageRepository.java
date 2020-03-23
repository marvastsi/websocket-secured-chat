package com.marvastsi.securedwebsocketchat.api.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.marvastsi.securedwebsocketchat.api.model.Message;
import com.marvastsi.securedwebsocketchat.api.model.User;
import com.marvastsi.securedwebsocketchat.socket.DTO.OutputMessageDTO;

@Repository
@Transactional
public interface MessageRepository extends JpaRepository<Message, String> {

	@Query("SELECT new com.marvastsi.securedwebsocketchat.socket.DTO.OutputMessageDTO("
			+ "m.id, m.type, m.content, m.sender.id, m.createdAt)"
			+ " FROM Message m "
			+ "   WHERE m.sender = :sender")
	Page<OutputMessageDTO> findBySender(@Param("sender") User sender, Pageable pageable);
	
	@Query("SELECT new com.marvastsi.securedwebsocketchat.socket.DTO.OutputMessageDTO(" 
			+ "m.id, m.type, m.content, m.sender.id, m.createdAt)"
			+ " FROM Message m "
			+ "   WHERE m.recipient = :recipient")
	Page<OutputMessageDTO> findByRecipient(@Param("recipient") User recipient, Pageable pageable);
}
