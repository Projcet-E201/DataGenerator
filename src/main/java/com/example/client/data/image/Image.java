package com.example.client.data.image;

import java.util.Base64;
import java.util.Random;

import com.example.client.data.global.AbstractData;
import com.example.client.netty.DataSender;
import com.example.client.util.DataInfo;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Image extends AbstractData<String> {
	public Image(DataSender dataSender, String dataType) {
		super(dataSender, dataType);
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
	public void dataSend(Channel channel) {
		sendDataScheduler.scheduleAtFixedRate(() -> {
			String data = dataQueue.poll();
			if (data != null) {
				// 데이터 구분자
				String dataConvent = data + "|";
				dataSender.sendData("IMAGE", dataType, dataConvent);
			}
		}, DataInfo.IMAGE_CALCULATE_TIME, DataInfo.IMAGE_CALCULATE_TIME, DataInfo.IMAGE_CALCULATE_TIME_UNIT);
	}
}
