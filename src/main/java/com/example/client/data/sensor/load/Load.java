package com.example.client.data.sensor.load;

import com.example.client.data.global.AbstractData;
import com.example.client.netty.DataSender;
import com.example.client.util.DataInfo;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Load extends AbstractData<Integer> {

	public Load(DataSender dataSender, String dataType) {
		super(dataSender, dataType);
	}

	@Override
	public void dataGenerate() {
		dataGenerationScheduler.scheduleAtFixedRate(() -> {
			Integer data = random.nextInt(17);
			dataQueue.offer(data);
		}, 0, DataInfo.LOAD_GENERATE_TIME, DataInfo.LOAD_GENERATE_TIME_UNIT);
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
		}, DataInfo.LOAD_CALCULATE_TIME, DataInfo.LOAD_CALCULATE_TIME, DataInfo.LOAD_CALCULATE_TIME_UNIT);
	}
}
