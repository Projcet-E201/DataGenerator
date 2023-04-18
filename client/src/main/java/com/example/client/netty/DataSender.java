package com.example.client.netty;

import org.springframework.stereotype.Component;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DataSender {

	private static final String CLIENT_NAME = "Server1";

	/**
	 *
	 * @param channel netty 채널
	 * @param dataType ex) MOTOR, AIR ...
	 */
	public <T> void sendData(Channel channel, String dataType,T data) {

		String combinedData = CLIENT_NAME + " " + dataType + " " + data;
		log.info("ClientName:{} DataType:{} CombineData:{}", CLIENT_NAME, dataType, combinedData);
		ChannelFuture future = channel.writeAndFlush(combinedData);

		future.addListener((ChannelFutureListener)channelFuture -> {
			if (!channelFuture.isSuccess()) {
				log.error("Failed to send data. Retrying...");
			} else {
				log.info("Data sent successfully.");
			}
		});
	}
}
