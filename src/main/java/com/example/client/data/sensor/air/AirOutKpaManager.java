package com.example.client.data.sensor.air;

import com.example.client.kafka.KafkaDataSender;
import org.springframework.stereotype.Component;

import com.example.client.data.global.AbstractDataManager;
import com.example.client.util.DataType;

@Component
public class AirOutKpaManager extends AbstractDataManager<AirOutKpa> {

	public AirOutKpaManager(KafkaDataSender kafkaDataSender) {
		super(kafkaDataSender, DataType.AIR_OUT_KPA);
	}

	@Override
	protected AirOutKpa createDataInstance(String dataType) {
		return new AirOutKpa(kafkaDataSender, dataType);
	}
}