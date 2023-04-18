package com.example.client.netty;

import java.net.InetSocketAddress;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

@Configuration
public class NettyClientConfiguration {
	@Value("${netty.host}")
	private String host;

	@Value("${netty.port}")
	private int port;

	@Bean
	public InetSocketAddress inetSocketAddress() {
		return new InetSocketAddress(host, port);
	}

	@Bean
	public ChannelWrapper channelWrapper() {
		return new ChannelWrapper();
	}

	@Bean
	public Bootstrap nettyBootstrap(NettyChannelInitializer nettyChannelInitializer) {
		NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();

		Bootstrap bootstrap = new Bootstrap()
			.group(eventLoopGroup) // 이벤트 루프 설정
			.channel(NioSocketChannel.class) // 소켓 입출력 모드 설정
			// .option(ChannelOption.TCP_NODELAY, true) // TCP_NODELAY 소켓 옵션을 설정(Nagle 알고리즘 사용 X)
			.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT) // PooledByteBufAllocator 사용
			.handler(nettyChannelInitializer);

		return bootstrap;
	}
}
