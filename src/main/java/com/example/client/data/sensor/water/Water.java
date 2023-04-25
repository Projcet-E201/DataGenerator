package com.example.client.data.sensor.water;

import com.example.client.data.global.AbstractData;
import com.example.client.netty.DataSender;
import com.example.client.util.DataInfo;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
public class Water extends AbstractData<Integer> {

	@Value("${client.name}")
	private String clientName;

	public Water(DataSender dataSender, String dataType) {
		super(dataSender, dataType);
	}

	@Override
	public void dataGenerate() {
		dataGenerationScheduler.scheduleAtFixedRate(() -> {
			Integer data = random.nextInt(401);
			dataQueue.offer(data);
		}, 0, DataInfo.WATER_GENERATE_TIME, DataInfo.WATER_GENERATE_TIME_UNIT);
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
		}, DataInfo.WATER_CALCULATE_TIME, DataInfo.WATER_CALCULATE_TIME, DataInfo.WATER_CALCULATE_TIME_UNIT);
	}
}