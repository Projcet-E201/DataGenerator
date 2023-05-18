package com.example.client.data.sensor.air;

import com.example.client.data.global.AbstractData;
import com.example.client.kafka.sender.ChunkDataSender;
import com.example.client.kafka.sender.DataSender;
import com.example.client.util.DataInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AirInKpa extends AbstractData<Integer> {

	public AirInKpa(DataSender dataSender, ChunkDataSender chunkDataSender, String dataType) {
		super(dataSender, chunkDataSender, dataType);
	}

	public void dataGenerate() {
		dataGenerationScheduler.scheduleAtFixedRate(() -> {
			Integer data = random.nextInt(901);
			dataQueue.offer(data);
		}, 0, DataInfo.AIR_IN_KPA_GENERATE_TIME, DataInfo.AIR_IN_KPA_GENERATE_TIME_UNIT);
	}

	@Override
	public void kafkaDataSend() {
		sendDataScheduler.scheduleAtFixedRate(() -> {
			if(!dataQueue.isEmpty()) {
				int value = dataQueue.poll();
				dataSender.sendData("AIR_IN_KPA", dataType, value);
			}
		}, DataInfo.AIR_IN_KPA_CALCULATE_TIME, DataInfo.AIR_IN_KPA_CALCULATE_TIME, DataInfo.AIR_IN_KPA_CALCULATE_TIME_UNIT);
	}
}
