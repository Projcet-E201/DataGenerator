package com.example.client.netty;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

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
	 * @param topic kafka topic 설정
	 * @param dataType ex) MOTOR, AIR ...
	 */
	public <T> void sendData(String topic, String dataType,T data) {

		// 데이터 전송시간 ex) 2023-04-17/10:12:34.123
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd/HH:mm:ss.SSS");
		String currentTime = LocalDateTime.now().format(formatter);

		String combinedData = clientName + " " + dataType + " " + data + " " + currentTime;

		// 머신별로 토픽을 나누고, 내부 파티션에서는 라운드로빈 방식으로 저장
		ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, combinedData);

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
