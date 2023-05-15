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

			dataSender.sendData("clientName", dataType, maxData);
		}, DataInfo.LOAD_CALCULATE_TIME, DataInfo.LOAD_CALCULATE_TIME, DataInfo.LOAD_CALCULATE_TIME_UNIT);
	}
}
