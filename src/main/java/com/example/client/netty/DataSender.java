package com.example.client.netty;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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
	public <T> void sendData(Channel channel, String dataType, T data) {
		// 데이터 전송시간 ex) 2023-04-17/10:12:34.123
		LocalDateTime currentTime = LocalDateTime.now();
		ZoneId seoulZoneId = ZoneId.of("Asia/Seoul");
		ZonedDateTime seoulTime = currentTime.atZone(seoulZoneId);
		LocalDateTime seoulLocalTime = seoulTime.toLocalDateTime();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd/HH:mm:ss");
		String formattedDateTime = seoulLocalTime.format(formatter);


		String combinedData = clientName + " " + dataType + " " + data + " " + formattedDateTime;

		ChannelFuture future = channel.writeAndFlush(combinedData);

		future.addListener((ChannelFutureListener)channelFuture -> {
			if (!channelFuture.isSuccess()) {
				log.error("Failed to send data. Retrying...");

				// TODO 실패시 로직 처리

			} else {
				log.info("Data sent successfully. Data:");
			}
		});
	}
}
