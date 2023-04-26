package com.example.client.data.sensor.abrasion;

import com.example.client.kafka.KafkaDataSender;
import org.springframework.stereotype.Component;

import com.example.client.data.global.AbstractDataManager;
import com.example.client.util.DataType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AbrasioManager extends AbstractDataManager<Abrasio> {

	public AbrasioManager(KafkaDataSender kafkaDataSender) {
		super(kafkaDataSender, DataType.ABRASION);
	}

	@Override
	protected Abrasio createDataInstance(String dataType) {
		return new Abrasio(kafkaDataSender, dataType);
	}
}