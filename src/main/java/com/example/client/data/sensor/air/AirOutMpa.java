package com.example.client.data.sensor.air;

import com.example.client.data.global.AbstractData;
import com.example.client.netty.DataSender;
import com.example.client.util.DataInfo;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AirOutMpa extends AbstractData<Double> {

	public AirOutMpa(DataSender dataSender, String dataType) {
		super(dataSender, dataType);
	}

	public void dataGenerate() {
		dataGenerationScheduler.scheduleAtFixedRate(() -> {
			Double data = -0.1 + 1.1 * random.nextDouble();
			dataQueue.offer(data);
		}, 0, DataInfo.AIR_OUT_MPA_GENERATE_TIME, DataInfo.AIR_OUT_MPA_GENERATE_TIME_UNIT);
	}

	public void dataSend(Channel channel) {
		sendDataScheduler.scheduleAtFixedRate(() -> {
				double maxData = Double.MIN_VALUE;
				Double data;
				while ((data = dataQueue.poll()) != null) {
					maxData = Math.max(maxData, data);
				}

				dataSender.sendData(channel, dataType, maxData);
			}, DataInfo.AIR_OUT_MPA_CALCULATE_TIME, DataInfo.AIR_OUT_MPA_CALCULATE_TIME,
			DataInfo.AIR_OUT_MPA_CALCULATE_TIME_UNIT);
	}
}
