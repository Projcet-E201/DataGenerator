package com.example.client.data.sensor.abrasion;

import com.example.client.data.global.AbstractData;
import com.example.client.netty.DataSender;
import com.example.client.util.DataInfo;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
public class Abrasio extends AbstractData<Integer> {

	@Value("${client.name}")
	private String clientName;

	public Abrasio(DataSender dataSender, String dataType) {
		super(dataSender, dataType);
	}

	@Override
	public void dataGenerate() {
		dataGenerationScheduler.scheduleAtFixedRate(() -> {
			Integer data = random.nextInt(41);
			dataQueue.offer(data);
		}, 0, DataInfo.ABRASION_GENERATE_TIME, DataInfo.ABRASION_GENERATE_TIME_UNIT);
	}

	@Override
	public void dataSend(Channel channel) {
		sendDataScheduler.scheduleAtFixedRate(() -> {
			int maxData = Integer.MIN_VALUE;
			Integer data;
			while ((data = dataQueue.poll()) != null) {
				maxData = Math.max(maxData, data);
			}

			dataSender.sendData(clientName, dataType, maxData);
		}, DataInfo.ABRASION_CALCULATE_TIME, DataInfo.ABRASION_CALCULATE_TIME, DataInfo.ABRASION_CALCULATE_TIME_UNIT);
	}
}