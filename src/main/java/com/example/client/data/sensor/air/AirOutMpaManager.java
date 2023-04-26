package com.example.client.data.sensor.air;

import com.example.client.kafka.KafkaDataSender;
import org.springframework.stereotype.Component;

import com.example.client.data.global.AbstractDataManager;
import com.example.client.util.DataType;

@Component
public class AirOutMpaManager extends AbstractDataManager<AirOutMpa> {

	public AirOutMpaManager(KafkaDataSender kafkaDataSender) {
		super(kafkaDataSender, DataType.AIR_OUT_MPA);
	}

	@Override
	protected AirOutMpa createDataInstance(String dataType) {
		return new AirOutMpa(kafkaDataSender, dataType);
	}
}