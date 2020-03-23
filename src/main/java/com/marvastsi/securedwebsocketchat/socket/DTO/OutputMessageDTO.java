package com.marvastsi.securedwebsocketchat.socket.DTO;

import java.time.LocalDateTime;

import com.marvastsi.securedwebsocketchat.api.model.Message;
import com.marvastsi.securedwebsocketchat.api.model.Message.MessageType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutputMessageDTO {
	private String id;
	private MessageType type;
	private String content;
	private String sender;
	private LocalDateTime createdAt;

	public OutputMessageDTO(Message message) {
		this.id = message.getId();
		this.type = message.getType();
		this.content = message.getContent();
		this.sender = message.getSender().getUsername();
		this.createdAt = message.getCreatedAt();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(this.getClass().getName());
		sb.append(": {");
		sb.append(" id: " + id);
		sb.append(" type: " + type);
		sb.append(", content: " + content);
		sb.append(", sender: " + sender);
		sb.append(", createdAt: " + createdAt);
		sb.append(" }");
		return sb.toString();
	}
}
