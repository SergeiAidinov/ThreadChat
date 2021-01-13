package ru.yandex.incoming34;

public class SeveralClients {

	public static void main(String[] args) {
		ThreadChatClient threadChatClient1 = new ThreadChatClient("Client 1", 8080);
		ThreadChatClient threadChatClient2 = new ThreadChatClient("Client 2", 8080);

	}

}
