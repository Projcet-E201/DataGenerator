package com.example.client.data.sensor.motor;

import com.example.client.kafka.KafkaDataSender;
import org.springframework.stereotype.Component;

import com.example.client.data.global.AbstractDataManager;
import com.example.client.util.DataType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MotorManager extends AbstractDataManager<Motor> {

	public MotorManager(KafkaDataSender kafkaDataSender) {
		super(kafkaDataSender, DataType.MOTOR);
	}

	@Override
	protected Motor createDataInstance(String dataType) {
		return new Motor(kafkaDataSender, dataType);
	}
}