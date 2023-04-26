package com.example.client.data.sensor.air;

import com.example.client.data.global.AbstractData;
import com.example.client.kafka.KafkaDataSender;
import com.example.client.util.DataInfo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
public class AirOutMpa extends AbstractData<Double> {

	@Value("${client.name}")
	private String clientName;

	public AirOutMpa(KafkaDataSender kafkaDataSender, String dataType) {
		super(kafkaDataSender, dataType);
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

			kafkaDataSender.sendData(clientName, dataType, maxData);
		}, DataInfo.AIR_OUT_MPA_CALCULATE_TIME, DataInfo.AIR_OUT_MPA_CALCULATE_TIME,
		DataInfo.AIR_OUT_MPA_CALCULATE_TIME_UNIT);
	}
}
