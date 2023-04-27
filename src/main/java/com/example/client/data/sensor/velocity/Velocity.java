package com.example.client.data.sensor.velocity;

import com.example.client.data.global.AbstractData;
import com.example.client.kafka.sender.ChunkDataSender;
import com.example.client.kafka.sender.DataSender;
import com.example.client.kafka.sender.SensorSender;
import com.example.client.util.DataInfo;
import lombok.extern.slf4j.Slf4j;

// Velocity 클래스 구현
@Slf4j
public class Velocity extends AbstractData<Integer> {

	public Velocity(DataSender dataSender, ChunkDataSender chunkDataSender, SensorSender sensorSender, String dataType) {
		super(dataSender, chunkDataSender, sensorSender, dataType);
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
	public void kafkaDataSend() {
		sendDataScheduler.scheduleAtFixedRate(() -> {
			int maxData = Integer.MIN_VALUE;
			Integer data;
			while ((data = dataQueue.poll()) != null) {
				maxData = Math.max(maxData, data);
			}

			sensorSender.sendData("clientName", dataType, maxData);
		}, DataInfo.VELOCITY_CALCULATE_TIME, DataInfo.VELOCITY_CALCULATE_TIME, DataInfo.VELOCITY_CALCULATE_TIME_UNIT);
	}
}