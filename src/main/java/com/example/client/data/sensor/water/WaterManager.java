package com.example.client.data.sensor.water;

import com.example.client.kafka.KafkaDataSender;
import org.springframework.stereotype.Component;

import com.example.client.data.global.AbstractDataManager;
import com.example.client.util.DataType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WaterManager extends AbstractDataManager<Water> {

	public WaterManager(KafkaDataSender kafkaDataSender) {
		super(kafkaDataSender, DataType.WATER);
	}

	@Override
	protected Water createDataInstance(String dataType) {
		return new Water(kafkaDataSender, dataType);
	}
}