package com.example.client.data.sensor.abrasion;

import com.example.client.data.global.AbstractData;
import com.example.client.kafka.sender.ChunkDataSender;
import com.example.client.kafka.sender.DataSender;
import com.example.client.util.DataInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Abrasio extends AbstractData<Integer> {

	public int[] time = new int[5];		// 센서 개수

	public Abrasio(DataSender dataSender, ChunkDataSender chunkDataSender, String dataType) {
		super(dataSender, chunkDataSender, dataType);
		for (int i = 0; i < 5; i++) {
			time[i] = 0;
		}
	}

	@Override
	public void dataGenerate() {
		dataGenerationScheduler.scheduleAtFixedRate(() -> {
			Integer data = random.nextInt(41);
			dataQueue.offer(data);
		}, 0, DataInfo.ABRASION_GENERATE_TIME, DataInfo.ABRASION_GENERATE_TIME_UNIT);
	}

	@Override
	public void kafkaDataSend() {
		sendDataScheduler.scheduleAtFixedRate(() -> {
			if(!dataQueue.isEmpty()) {
				int value = dataQueue.poll();
				dataSender.sendData("ABRASION", dataType, value);
			}
		}, DataInfo.ABRASION_CALCULATE_TIME, DataInfo.ABRASION_CALCULATE_TIME, DataInfo.ABRASION_CALCULATE_TIME_UNIT);
	}
}