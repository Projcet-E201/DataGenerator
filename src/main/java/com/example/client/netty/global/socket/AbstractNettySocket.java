package com.example.client.netty.global.socket;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PreDestroy;

import com.example.client.data.global.AbstractDataManager;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractNettySocket {
	protected final Bootstrap bootstrap;
	protected final InetSocketAddress tcpPort;

	private Channel clientChannel;
	protected List<AbstractDataManager<?>> managers = new ArrayList<>();

	public AbstractNettySocket(Bootstrap bootstrap, InetSocketAddress tcpPort) {
		this.bootstrap = bootstrap;
		this.tcpPort = tcpPort;
	}

	public void connect() {
		// ChannelFuture: I/O operation의 결과나 상태를 제공하는 객체
		// 지정한 host, port로 소켓을 바인딩하고 incoming connections을 받도록 준비함

		// 서버에 연결하고, 연결이 완료될 때까지 대기합니다.
		ChannelFuture clientChannelFuture = bootstrap.connect(tcpPort);

		// 연결 상태를 확인하기 위한 ChannelFutureListener 추가합니다.
		clientChannelFuture.addListener((ChannelFutureListener) future -> {

			// 연결 성공
			if (future.isSuccess()) {
				Channel channel = future.channel();
				log.info("Connected to the server successfully.");

				managers.forEach(manager -> manager.sendData(channel));

			}
			// 연결실패
			else {
				log.error("Failed to connect to the server. Cause: ", future.cause());
			}
		});

		// 동기화를 대신에 채널에서 수행합니다.
		clientChannel = clientChannelFuture.channel();
		clientChannel.closeFuture().addListener((ChannelFuture future) -> log.info("Server channel closed."));
	}

	@PreDestroy
	public void stop() {
		if (clientChannel != null) {
			clientChannel.close();
			clientChannel.parent().closeFuture();
		}
	}
}