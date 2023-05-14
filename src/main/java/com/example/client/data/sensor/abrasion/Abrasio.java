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
			final int[] slopes = {1, 3, 4, 2, 5};		// 기울기 설정

			int idx = Integer.parseInt(dataType.replaceAll("[^0-9]", "")) - 1;
			System.out.println(time[idx]);
			int data = slopes[idx] * time[idx];
			time[idx] += 1;

			if(data + slopes[idx] > 40) {
				time[idx] = 0;
			}

			dataQueue.offer(data);
		}, 0, DataInfo.ABRASION_GENERATE_TIME, DataInfo.ABRASION_GENERATE_TIME_UNIT);
	}

	@Override
	public void kafkaDataSend() {
		sendDataScheduler.scheduleAtFixedRate(() -> {
			Integer data;
			while ((data = dataQueue.poll()) != null) {
				dataSender.sendData("clientName", dataType, data);
			}
		}, DataInfo.ABRASION_CALCULATE_TIME, DataInfo.ABRASION_CALCULATE_TIME, DataInfo.ABRASION_CALCULATE_TIME_UNIT);
	}
}