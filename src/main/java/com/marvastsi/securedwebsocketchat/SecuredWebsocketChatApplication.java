package com.marvastsi.securedwebsocketchat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@SpringBootApplication
public class SecuredWebsocketChatApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecuredWebsocketChatApplication.class, args);
	}

}
