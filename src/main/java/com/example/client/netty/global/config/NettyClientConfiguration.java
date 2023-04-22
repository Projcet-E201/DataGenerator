package com.example.client.netty.global.config;

import java.net.InetSocketAddress;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.client.netty.analog.AnalogNettyChannelInitializer;
import com.example.client.netty.data.DataNettyChannelInitializer;
import com.example.client.netty.image.ImageNettyChannelInitializer;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

@Configuration
public class NettyClientConfiguration {
	@Value("${netty.host}")
	private String host;

	@Value("${netty.data-port}")
	private int dataPort;
	@Value("${netty.analog-port}")
	private int analogPort;
	@Value("${netty.image-port}")
	private int imagePort;


	@Bean
	public InetSocketAddress dataInetSocketAddress() {
		return new InetSocketAddress(host, dataPort);
	}
	@Bean
	public InetSocketAddress analogInitSocketAddress() {
		return new InetSocketAddress(host,analogPort);
	}
	@Bean
	public InetSocketAddress imageInitSocketAddress() {
		return new InetSocketAddress(host,imagePort);
	}


	@Bean
	public Bootstrap dataServerBootstrap(DataNettyChannelInitializer dataNettyChannelInitializer) {
		NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();

		return new Bootstrap()
			.group(eventLoopGroup) // 이벤트 루프 설정
			.channel(NioSocketChannel.class) // 소켓 입출력 모드 설정
			.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT) // PooledByteBufAllocator 사용
			.handler(dataNettyChannelInitializer);
	}

	@Bean
	public Bootstrap analogServerBootstrap(AnalogNettyChannelInitializer analogNettyChannelInitializer) {
		NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();

		return new Bootstrap()
			.group(eventLoopGroup) // 이벤트 루프 설정
			.channel(NioSocketChannel.class) // 소켓 입출력 모드 설정
			.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT) // PooledByteBufAllocator 사용
			.handler(analogNettyChannelInitializer);
	}

	@Bean
	public Bootstrap imageServerBootstrap(ImageNettyChannelInitializer imageNettyChannelInitializer) {
		NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();

		return new Bootstrap()
			.group(eventLoopGroup) // 이벤트 루프 설정
			.channel(NioSocketChannel.class) // 소켓 입출력 모드 설정
			.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT) // PooledByteBufAllocator 사용
			.handler(imageNettyChannelInitializer);
	}

}
