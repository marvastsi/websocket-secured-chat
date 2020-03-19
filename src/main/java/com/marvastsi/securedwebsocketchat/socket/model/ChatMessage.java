package com.marvastsi.securedwebsocketchat.socket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
	private MessageType type;
	private String content;
	private String sender;

	public enum MessageType {
		CHAT, JOIN, LEAVE
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("ChatMessage {");
		sb.append(" type: " + type);
		sb.append(", sender: " + sender);
		sb.append(", content: " + content);
		sb.append(" }");
		return sb.toString();
	}
}
