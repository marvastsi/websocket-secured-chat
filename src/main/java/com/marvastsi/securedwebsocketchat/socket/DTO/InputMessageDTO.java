package com.marvastsi.securedwebsocketchat.socket.DTO;

import com.marvastsi.securedwebsocketchat.api.model.Message.MessageType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InputMessageDTO {
	private MessageType type;
	private String content;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(this.getClass().getName());
		sb.append(": {");
		sb.append(" type: " + type);
		sb.append(", content: " + content);
		sb.append(" }");
		return sb.toString();
	}
}
