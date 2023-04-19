package com.example.client.data.sensor.velocity;

import com.example.client.data.global.AbstractData;
import com.example.client.netty.DataSender;
import com.example.client.util.DataInfo;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

// Velocity 클래스 구현
@Slf4j
public class Velocity extends AbstractData<Integer> {

	public Velocity(DataSender dataSender, String dataType) {
		super(dataSender, dataType);
	}

	// Velocity 클래스 구현
	@Override
	public void dataGenerate() {
		dataGenerationScheduler.scheduleAtFixedRate(() -> {
			Integer data = random.nextInt(50001);
			dataQueue.offer(data);
		}, 0, DataInfo.VELOCITY_GENERATE_TIME, DataInfo.VELOCITY_GENERATE_TIME_UNIT);
	}

	@Override
	public void dataSend(Channel channel) {
		sendDataScheduler.scheduleAtFixedRate(() -> {
			int maxData = Integer.MIN_VALUE;
			Integer data;
			while ((data = dataQueue.poll()) != null) {
				maxData = Math.max(maxData, data);
			}

			dataSender.sendData(channel, dataType, maxData);
		}, DataInfo.VELOCITY_CALCULATE_TIME, DataInfo.VELOCITY_CALCULATE_TIME, DataInfo.VELOCITY_CALCULATE_TIME_UNIT);
	}
}