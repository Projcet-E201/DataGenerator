package com.example.client.netty.image;

import java.util.UUID;

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
public class ImageNettyOutboundHandler extends MessageToByteEncoder<String> {
	@Override
	protected void encode(ChannelHandlerContext ctx, String msg, ByteBuf out) {
		// 데이터 구분자 추가
		UUID uuid = UUID.randomUUID();
		msg += " " + uuid;

		byte[] messageBytes = msg.getBytes(CharsetUtil.UTF_8);
		int maxLength = 1024; // 데이터를 분할할 최대 길이 설정 (적절한 값을 사용하세요)

		for (int i = 0; i < messageBytes.length; i += maxLength) {
			int length = Math.min(maxLength, messageBytes.length - i);
			ByteBuf messagePart = Unpooled.copiedBuffer(messageBytes, i, length);

			out.writeBytes(messagePart);
		}

		ByteBuf delimiter = Unpooled.copiedBuffer("\n", CharsetUtil.UTF_8);
		out.writeBytes(delimiter);
	}
}
