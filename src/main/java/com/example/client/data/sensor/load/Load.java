package com.example.client.data.sensor.load;

import com.example.client.data.global.AbstractData;
import com.example.client.kafka.sender.ChunkDataSender;
import com.example.client.kafka.sender.DataSender;
import com.example.client.kafka.sender.SensorSender;
import com.example.client.util.DataInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Load extends AbstractData<Integer> {

	public Load(DataSender dataSender, ChunkDataSender chunkDataSender, SensorSender sensorSender, String dataType) {
		super(dataSender, chunkDataSender, sensorSender, dataType);
	}

	@Override
	public void dataGenerate() {
		dataGenerationScheduler.scheduleAtFixedRate(() -> {
			Integer data = random.nextInt(17);
			dataQueue.offer(data);
		}, 0, DataInfo.LOAD_GENERATE_TIME, DataInfo.LOAD_GENERATE_TIME_UNIT);
	}

	@Override
	public void kafkaDataSend() {
		sendDataScheduler.scheduleAtFixedRate(() -> {
			int maxData = Integer.MIN_VALUE;
			Integer data;
			while ((data = dataQueue.poll()) != null) {
				maxData = Math.max(maxData, data);
			}

			sensorSender.sendData("clientName", dataType, maxData);
		}, DataInfo.LOAD_CALCULATE_TIME, DataInfo.LOAD_CALCULATE_TIME, DataInfo.LOAD_CALCULATE_TIME_UNIT);
	}
}
