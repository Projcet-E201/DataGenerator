package com.example.client.netty;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DataSender {

	@Value("${client.name}")
	private String clientName;

	/**
	 *
	 * @param channel netty 채널
	 * @param dataType ex) MOTOR, AIR ...
	 */
	public <T> void sendData(Channel channel, String dataType,T data) {

		String combinedData = clientName + " " + dataType + " " + data;
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
