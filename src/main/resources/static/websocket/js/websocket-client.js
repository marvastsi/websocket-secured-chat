"use strict";

var loginPage = document.querySelector("#loginPage");
var chatPage = document.querySelector("#chatPage");
var loginForm = document.querySelector("#loginForm");
var messageForm = document.querySelector("#messageForm");
var messageInput = document.querySelector("#message");
var recipientInput = document.querySelector("#recipient");
var messageContainer = document.querySelector("#messageContainer");
var connectingElement = document.querySelector("#connecting");

var stompClient = null;
var username = null;

var colors = [ "#2196F3", "#32C787", "#00BCD4", "#FF5652", "#FFC107",
		"#FF85AF", "#FF9800", "#39BBB0" ];

var auth = {};

function connect(event) {
	username = document.querySelector("#username").value.trim();
	auth[username] = document.querySelector("#authtoken").value.trim();

	if (username) {
		loginPage.classList.add("hidden");
		connectingElement.classList.remove("hidden");
		chatPage.classList.remove("hidden");

		// Bind server
		var socket = new SockJS("/api/ws");
		stompClient = Stomp.over(socket);

		// Add header access token here
		var authToken = auth[username];
		// Try connect to the server
		stompClient.connect({
			"X-Authorization" : `Bearer ${authToken}`
		}, onConnected, onError);
		
//		// Try connect to the server without authentication
//		stompClient.connect({}, onConnected, onError);
	}
	event.preventDefault();
}

function onConnected() {
	// Subscribe to the Public Topic
	stompClient.subscribe("/channel/public", onMessageReceived);
	// Subscribe to the authenticated user channel
	stompClient.subscribe(`/channel/${username}`, onMessageReceived);

	// Send message to the public channel
	stompClient.send(`/app/chat.sendMessage`, {}, JSON.stringify({
		sender : username,
		type : "JOIN"
	}))

	connectingElement.classList.add("hidden");
}

function onError(error) {
	connectingElement.textContent = "Could not connect to the WebSocket server. Try refresh this page."
			+ error;
	connectingElement.style.color = "#F00";
}

function sendMessage(event) {
	var messageContent = messageInput.value.trim();
	var recipient = recipientInput.value.trim();

	if (messageContent && stompClient) {
		var chatMessage = {
			content : messageInput.value,
			type : "CHAT"
		};

		let destination = recipient ? `/app/chat.sendMessage/${recipient}` : "/app/chat.sendMessage";  
		stompClient.send("/app/chat.sendMessage", {}, JSON
				.stringify(chatMessage));

		messageInput.value = "";
		recipientInput.value = "";
	}
	event.preventDefault();
}

function onMessageReceived(payload) {
	var message = JSON.parse(payload.body);

	var messageListElement = document.createElement("li");

	switch (message.type) {
	case "JOIN":
		messageListElement.classList.add("event-message");
		console.log(message);
		message.content = message.sender + " joined!";
		break;
	case "LEAVE":
		messageListElement.classList.add("event-message");
		message.content = message.sender + " left!";
		break;
	default:
		messageListElement.classList.add("chat-message");

		var avatarElement = document.createElement("i");
		var avatarText = document.createTextNode(message.sender[0]);
		avatarElement.appendChild(avatarText);
		avatarElement.style["background-color"] = getAvatarColor(message.sender);

		messageListElement.appendChild(avatarElement);

		var spanElement = document.createElement("span");
		var spanText = document.createTextNode(message.sender);
		spanElement.appendChild(spanText);
		messageListElement.appendChild(spanElement);
	}

	var pElement = document.createElement("p");
	var messageText = document.createTextNode(message.content);
	pElement.appendChild(messageText);

	messageListElement.appendChild(pElement);

	messageContainer.appendChild(messageListElement);
	messageContainer.scrollTop = messageContainer.scrollHeight;
}

function getAvatarColor(messageSender) {
	var hash = 0;
	for (var i = 0; i < messageSender.length; i++) {
		hash = 31 * hash + messageSender.charCodeAt(i);
	}

	var index = Math.abs(hash % colors.length);
	return colors[index];
}

loginForm.addEventListener("submit", connect, true)
messageForm.addEventListener("submit", sendMessage, true)
