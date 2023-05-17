package com.example.client.data.sensor.water;

import com.example.client.data.global.AbstractData;
import com.example.client.kafka.sender.ChunkDataSender;
import com.example.client.kafka.sender.DataSender;
import com.example.client.util.DataInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Water extends AbstractData<Integer> {

	public Water(DataSender dataSender, ChunkDataSender chunkDataSender, String dataType) {
		super(dataSender, chunkDataSender, dataType);
	}

	@Override
	public void dataGenerate() {
		dataGenerationScheduler.scheduleAtFixedRate(() -> {
			Integer data = random.nextInt(5);
			dataQueue.offer(data);
		}, 0, DataInfo.WATER_GENERATE_TIME, DataInfo.WATER_GENERATE_TIME_UNIT);
	}

	@Override
	public void kafkaDataSend() {
		sendDataScheduler.scheduleAtFixedRate(() -> {
			if(!dataQueue.isEmpty()) {
				int value = dataQueue.poll();
				dataSender.sendData("WATER", dataType, value);
			}
		}, DataInfo.WATER_CALCULATE_TIME, DataInfo.WATER_CALCULATE_TIME, DataInfo.WATER_CALCULATE_TIME_UNIT);
	}
}