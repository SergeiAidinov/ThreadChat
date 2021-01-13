package ru.yandex.incoming34;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

public class ThreadChatClient implements ChannelWriter, Runnable {

	public ThreadChatClient(String message, int port) {
		super();
		this.message = message;
		this.port = port;
		this.hostAddress = new InetSocketAddress(port);
		thread = new Thread(this);
		thread.start();
	}

	private final InetSocketAddress hostAddress;
	private String message;
	private int port;
	Thread thread;

	private ThreadChatClient(final int port) {
		this.hostAddress = new InetSocketAddress(port);
	}

	@Override
	public void run() {
		assert StringUtils.isNotEmpty(message);

		try (SocketChannel client = SocketChannel.open(this.hostAddress)) {

			final ByteBuffer buffer = ByteBuffer.wrap((message + Constants.END_MESSAGE_MARKER).trim().getBytes());

			doWrite(buffer, client);

			buffer.flip();

			final StringBuilder echo = new StringBuilder();
			doRead(echo, buffer, client);

			System.out.println(String.format("Message :\t %s \nEcho    :\t %s", message,
					echo.toString().replace(Constants.END_MESSAGE_MARKER, StringUtils.EMPTY)));
		} catch (IOException e) {
			throw new RuntimeException("Unable to communicate with server.", e);
		}
	}

	private void doRead(final StringBuilder data, final ByteBuffer buffer, final SocketChannel channel)
			throws IOException {
		assert !Objects.isNull(data) && !Objects.isNull(buffer) && !Objects.isNull(channel);

		while (channel.read(buffer) != -1) {
			data.append(new String(buffer.array()).trim());
			buffer.clear();
		}
	}

}
