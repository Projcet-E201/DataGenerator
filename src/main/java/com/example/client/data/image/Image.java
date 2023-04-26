package com.example.client.data.image;

import java.util.Base64;
import java.util.Random;

import com.example.client.data.global.AbstractData;
import com.example.client.kafka.KafkaDataSender;
import com.example.client.util.DataInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Image extends AbstractData<String> {
	public Image(KafkaDataSender kafkaDataSender, String dataType) {
		super(kafkaDataSender, dataType);
	}

	@Override
	public void dataGenerate() {
		dataGenerationScheduler.scheduleAtFixedRate(() -> {
			byte[] randomImageData = new byte[3 * 1024 * 1024]; // 3MB
			new Random().nextBytes(randomImageData);
			String encoded = Base64.getEncoder().withoutPadding().encodeToString(randomImageData);
			dataQueue.offer(encoded);
		}, 0, DataInfo.IMAGE_GENERATE_TIME, DataInfo.IMAGE_GENERATE_TIME_UNIT);
	}

	@Override
	public void kafkaDataSend() {
		sendDataScheduler.scheduleAtFixedRate(() -> {
			String data = dataQueue.poll();
			if (data != null) {
				// 데이터 구분자
				String dataConvent = data + "|";
				kafkaDataSender.sendData("IMAGE", dataType, dataConvent);
			}
		}, DataInfo.IMAGE_CALCULATE_TIME, DataInfo.IMAGE_CALCULATE_TIME, DataInfo.IMAGE_CALCULATE_TIME_UNIT);
	}
}
