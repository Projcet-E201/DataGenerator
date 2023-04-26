package com.example.client.data.sensor.load;

import com.example.client.data.global.AbstractData;
import com.example.client.kafka.KafkaDataSender;
import com.example.client.util.DataInfo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
public class Load extends AbstractData<Integer> {

	@Value("${client.name}")
	private String clientName;

	public Load(KafkaDataSender kafkaDataSender, String dataType) {
		super(kafkaDataSender, dataType);
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

			kafkaDataSender.sendData(clientName, dataType, maxData);
		}, DataInfo.LOAD_CALCULATE_TIME, DataInfo.LOAD_CALCULATE_TIME, DataInfo.LOAD_CALCULATE_TIME_UNIT);
	}
}
