package com.example.client.data.sensor.motor;

import com.example.client.data.global.AbstractData;
import com.example.client.kafka.sender.ChunkDataSender;
import com.example.client.kafka.sender.DataSender;
import com.example.client.util.DataInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Motor extends AbstractData<Integer> {

	public Motor(DataSender dataSender, ChunkDataSender chunkDataSender, String dataType) {
		super(dataSender, chunkDataSender, dataType);
	}

	@Override
	public void dataGenerate() {
		dataGenerationScheduler.scheduleAtFixedRate(() -> {
			Integer data = random.nextInt(601) - 300;
			dataQueue.offer(data);
		}, 0, DataInfo.MOTOR_GENERATE_TIME, DataInfo.MOTOR_GENERATE_TIME_UNIT);
	}

	@Override
	public void kafkaDataSend() {
		sendDataScheduler.scheduleAtFixedRate(() -> {
			if(!dataQueue.isEmpty()) {
				int value = dataQueue.poll();
				dataSender.sendData("MOTOR", dataType, value);
			}
		}, DataInfo.MOTOR_CALCULATE_TIME, DataInfo.MOTOR_CALCULATE_TIME, DataInfo.MOTOR_CALCULATE_TIME_UNIT);
	}
}
