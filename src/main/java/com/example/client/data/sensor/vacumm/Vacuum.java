package com.example.client.data.sensor.vacumm;

import com.example.client.data.global.AbstractData;
import com.example.client.kafka.sender.ChunkDataSender;
import com.example.client.kafka.sender.DataSender;
import com.example.client.util.DataInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Vacuum extends AbstractData<Integer> {

	public Vacuum(DataSender dataSender, ChunkDataSender chunkDataSender, String dataType) {
		super(dataSender, chunkDataSender, dataType);
	}

	@Override
	public void dataGenerate() {
		dataGenerationScheduler.scheduleAtFixedRate(() -> {
			Integer data = random.nextInt(131) - 100;
			dataQueue.offer(data);
		}, 0, DataInfo.VACUUM_GENERATE_TIME, DataInfo.VACUUM_GENERATE_TIME_UNIT);
	}

	@Override
	public void kafkaDataSend() {
		sendDataScheduler.scheduleAtFixedRate(() -> {
			int maxData = Integer.MIN_VALUE;
			Integer data;
			while ((data = dataQueue.poll()) != null) {
				maxData = Math.max(maxData, data);
			}

			dataSender.sendData("clientName", dataType, maxData);
		}, DataInfo.VACUUM_CALCULATE_TIME, DataInfo.VACUUM_CALCULATE_TIME, DataInfo.VACUUM_CALCULATE_TIME_UNIT);
	}
}
