package com.example.client.data.sensor.air;

import com.example.client.data.global.AbstractData;
import com.example.client.kafka.sender.ChunkDataSender;
import com.example.client.kafka.sender.DataSender;
import com.example.client.util.DataInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AirOutMpa extends AbstractData<Double> {

	public AirOutMpa(DataSender dataSender, ChunkDataSender chunkDataSender, String dataType) {
		super(dataSender, chunkDataSender, dataType);
	}

	public void dataGenerate() {
		dataGenerationScheduler.scheduleAtFixedRate(() -> {
			Double data = -0.1 + 1.1 * random.nextDouble();
			dataQueue.offer(data);
		}, 0, DataInfo.AIR_OUT_MPA_GENERATE_TIME, DataInfo.AIR_OUT_MPA_GENERATE_TIME_UNIT);
	}

	@Override
	public void kafkaDataSend() {
		sendDataScheduler.scheduleAtFixedRate(() -> {
			double maxData = Double.MIN_VALUE;
			Double data;
			while ((data = dataQueue.poll()) != null) {
				maxData = Math.max(maxData, data);
			}

			dataSender.sendData("clientName", dataType, maxData);
		}, DataInfo.AIR_OUT_MPA_CALCULATE_TIME, DataInfo.AIR_OUT_MPA_CALCULATE_TIME,
		DataInfo.AIR_OUT_MPA_CALCULATE_TIME_UNIT);
	}
}
