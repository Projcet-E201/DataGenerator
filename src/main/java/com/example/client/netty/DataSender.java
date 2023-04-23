package com.example.client.netty;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataSender {

	@Value("${client.name}")
	private String clientName;

	private final KafkaTemplate kafkaTemplate;

	/**
	 *
	 * @param channel netty 채널
	 * @param dataType ex) MOTOR, AIR ...
	 */
	public <T> void sendData(Channel channel, String dataType, T data) {

		// 데이터 전송시간 ex) 2023-04-17/10:12:34.123
		LocalDateTime currentTime = LocalDateTime.now();
		long unixTimestamp = currentTime.toEpochSecond(ZoneOffset.UTC);

		String combinedData = clientName + " " + dataType + " " + data + " " + unixTimestamp;

		System.out.println("===============> send data : " + combinedData);
		ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send("Test", combinedData);

		future.addCallback(new ListenableFutureCallback<>() {
			@Override
			public void onFailure(Throwable ex) {
				System.err.println("Error while sending message: " + ex.getMessage());
			}

			@Override
			public void onSuccess(SendResult<String, String> result) {
				RecordMetadata metadata = result.getRecordMetadata();
				System.out.println("Message sent to partition " + metadata.partition() +
						" with offset " + metadata.offset());
			}
		});
	}
}
