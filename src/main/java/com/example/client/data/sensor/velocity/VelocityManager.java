package com.example.client.data.sensor.velocity;

import com.example.client.kafka.KafkaDataSender;
import org.springframework.stereotype.Component;

import com.example.client.data.global.AbstractDataManager;
import com.example.client.util.DataType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class VelocityManager extends AbstractDataManager<Velocity> {

	public VelocityManager(KafkaDataSender kafkaDataSender) {
		super(kafkaDataSender, DataType.VELOCITY);
	}

	@Override
	protected Velocity createDataInstance(String dataType) {
		return new Velocity(kafkaDataSender, dataType);
	}
}