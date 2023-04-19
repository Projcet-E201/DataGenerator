package com.example.client.data.sensor.air;

import com.example.client.data.global.AbstractData;
import com.example.client.netty.DataSender;
import com.example.client.util.DataInfo;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AirInKpa extends AbstractData<Integer> {

	public AirInKpa(DataSender dataSender, String dataType) {
		super(dataSender, dataType);
	}

	public void dataGenerate() {
		dataGenerationScheduler.scheduleAtFixedRate(() -> {
			Integer data = random.nextInt(901);
			dataQueue.offer(data);
		}, 0, DataInfo.AIR_IN_KPA_GENERATE_TIME, DataInfo.AIR_IN_KPA_GENERATE_TIME_UNIT);
	}

	public void dataSend(Channel channel) {
		sendDataScheduler.scheduleAtFixedRate(() -> dataSender.sendData(channel, dataType, dataQueue.poll()), DataInfo.AIR_IN_KPA_CALCULATE_TIME, DataInfo.AIR_IN_KPA_CALCULATE_TIME,
			DataInfo.AIR_IN_KPA_CALCULATE_TIME_UNIT);
	}
}
