package com.example.client.data.image;

import com.example.client.data.global.AbstractData;
import com.example.client.kafka.sender.ChunkDataSender;
import com.example.client.kafka.sender.DataSender;
import com.example.client.util.DataInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;
import java.util.Random;

@Slf4j
public class Image extends AbstractData<String> {
	public Image(DataSender dataSender, ChunkDataSender chunkDataSender, String dataType) {
		super(dataSender, chunkDataSender, dataType);
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
				chunkDataSender.sendData("IMAGE", dataType, dataConvent);
			}
		}, DataInfo.IMAGE_CALCULATE_TIME, DataInfo.IMAGE_CALCULATE_TIME, DataInfo.IMAGE_CALCULATE_TIME_UNIT);
	}
}
