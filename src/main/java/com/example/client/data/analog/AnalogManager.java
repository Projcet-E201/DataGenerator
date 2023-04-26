package com.example.client.data.analog;

import com.example.client.kafka.KafkaDataSender;
import org.springframework.stereotype.Component;

import com.example.client.data.global.AbstractDataManager;
import com.example.client.util.DataType;

@Component
public class AnalogManager extends AbstractDataManager<Analog> {

	public AnalogManager(KafkaDataSender kafkaDataSender) {
		super(kafkaDataSender, DataType.ANALOG);
	}

	@Override
	protected Analog createDataInstance(String dataType) {
		return new Analog(kafkaDataSender, dataType);
	}
}
