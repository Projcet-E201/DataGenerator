package com.example.client.data.sensor.velocity;

import com.example.client.data.global.AbstractData;
import com.example.client.kafka.KafkaDataSender;
import com.example.client.util.DataInfo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

// Velocity 클래스 구현
@Slf4j
public class Velocity extends AbstractData<Integer> {

	@Value("${client.name}")
	private String clientName;

	public Velocity(KafkaDataSender kafkaDataSender, String dataType) {
		super(kafkaDataSender, dataType);
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

			kafkaDataSender.sendData(clientName, dataType, maxData);
		}, DataInfo.VELOCITY_CALCULATE_TIME, DataInfo.VELOCITY_CALCULATE_TIME, DataInfo.VELOCITY_CALCULATE_TIME_UNIT);
	}
}