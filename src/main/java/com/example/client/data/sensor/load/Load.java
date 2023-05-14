package com.example.client.data.sensor.load;

import com.example.client.data.global.AbstractData;
import com.example.client.kafka.sender.ChunkDataSender;
import com.example.client.kafka.sender.DataSender;
import com.example.client.util.DataInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Load extends AbstractData<Integer> {

	public int[] time = new int[5];		// 센서 개수

	public Load(DataSender dataSender, ChunkDataSender chunkDataSender, String dataType) {
		super(dataSender, chunkDataSender, dataType);
		for (int i = 0; i < 5; i++) {
			time[i] = 0;
		}
	}

	@Override
	public void dataGenerate() {
		dataGenerationScheduler.scheduleAtFixedRate(() -> {
			final int[] slopes = {2, 4, 1, 5, 3};		// 기울기 설정

			int idx = Integer.parseInt(dataType.replaceAll("[^0-9]", "")) - 1;
			System.out.println(time[idx]);
			int data = slopes[idx] * time[idx];
			time[idx] += 1;

			if(data + slopes[idx] > 16) {
				time[idx] = 0;
			}

			dataQueue.offer(data);
		}, 0, DataInfo.LOAD_GENERATE_TIME, DataInfo.LOAD_GENERATE_TIME_UNIT);
	}

	@Override
	public void kafkaDataSend() {
		sendDataScheduler.scheduleAtFixedRate(() -> {
			Integer data;
			while ((data = dataQueue.poll()) != null) {
				dataSender.sendData("clientName", dataType, data);
			}
		}, DataInfo.LOAD_CALCULATE_TIME, DataInfo.LOAD_CALCULATE_TIME, DataInfo.LOAD_CALCULATE_TIME_UNIT);
	}
}
