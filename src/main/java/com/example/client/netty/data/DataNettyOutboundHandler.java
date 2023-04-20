package com.example.client.netty.data;

import org.springframework.stereotype.Component;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ChannelHandler.Sharable
@RequiredArgsConstructor
public class DataNettyOutboundHandler extends MessageToByteEncoder<String> {
	@Override
	protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) {
		ByteBuf message = Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8);
		ByteBuf delimiter = Unpooled.copiedBuffer("\n", CharsetUtil.UTF_8);
		ByteBuf combinedMessage = Unpooled.wrappedBuffer(message, delimiter);

		out.writeBytes(combinedMessage);
	}
}
