package com.example.client.data.sensor.air;

import com.example.client.data.global.AbstractData;
import com.example.client.kafka.sender.ChunkDataSender;
import com.example.client.kafka.sender.DataSender;
import com.example.client.util.DataInfo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AirOutKpa extends AbstractData<Integer> {

	public AirOutKpa(DataSender dataSender, ChunkDataSender chunkDataSender, String dataType) {
		super(dataSender, chunkDataSender, dataType);
	}

	public void dataGenerate() {
		// 주기적으로 데이터를 생성하는 코드
		dataGenerationScheduler.scheduleAtFixedRate(() -> {
			Integer data = random.nextInt(901);
			dataQueue.offer(data);
		}, 0, DataInfo.AIR_OUT_KPA_GENERATE_TIME, DataInfo.AIR_OUT_KPA_GENERATE_TIME_UNIT);
	}

	@Override
	public void kafkaDataSend() {
		// 30초마다 생성된 데이터 중 최대값을 찾는 코드
		sendDataScheduler.scheduleAtFixedRate(() -> {
			int maxData = Integer.MIN_VALUE;
			Integer data;
			while ((data = dataQueue.poll()) != null) {
				maxData = Math.max(maxData, data);
			}

			dataSender.sendData("clientName", dataType, maxData);
		}, DataInfo.AIR_OUT_KPA_CALCULATE_TIME, DataInfo.AIR_OUT_KPA_CALCULATE_TIME,
		DataInfo.AIR_OUT_KPA_CALCULATE_TIME_UNIT);
	}
}
