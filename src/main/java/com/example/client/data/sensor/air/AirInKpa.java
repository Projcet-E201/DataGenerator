package com.example.client.data.sensor.air;

import com.example.client.data.global.AbstractData;
import com.example.client.kafka.KafkaDataSender;
import com.example.client.util.DataInfo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
public class AirInKpa extends AbstractData<Integer> {

	@Value("${client.name}")
	private String clientName;

	public AirInKpa(KafkaDataSender kafkaDataSender, String dataType) {
		super(kafkaDataSender, dataType);
	}

	public void dataGenerate() {
		dataGenerationScheduler.scheduleAtFixedRate(() -> {
			Integer data = random.nextInt(901);
			dataQueue.offer(data);
		}, 0, DataInfo.AIR_IN_KPA_GENERATE_TIME, DataInfo.AIR_IN_KPA_GENERATE_TIME_UNIT);
	}

	@Override
	public void kafkaDataSend() {
		sendDataScheduler.scheduleAtFixedRate(
				() -> kafkaDataSender.sendData(clientName, dataType, dataQueue.poll()),
				DataInfo.AIR_IN_KPA_CALCULATE_TIME, DataInfo.AIR_IN_KPA_CALCULATE_TIME,
				DataInfo.AIR_IN_KPA_CALCULATE_TIME_UNIT);
	}
}
