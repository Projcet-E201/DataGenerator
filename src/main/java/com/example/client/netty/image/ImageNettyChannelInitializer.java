package com.example.client.netty.image;

import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ChannelHandler.Sharable
@RequiredArgsConstructor
public class ImageNettyChannelInitializer extends ChannelInitializer<SocketChannel> {

	private final ImageNettyOutboundHandler imageNettyOutboundHandler;

	@Override
	protected void initChannel(SocketChannel socketChannel) {
		ChannelPipeline pipeline = socketChannel.pipeline();

		// Inbound 핸들러 등록
		pipeline.addLast(imageNettyOutboundHandler);
	}


}
