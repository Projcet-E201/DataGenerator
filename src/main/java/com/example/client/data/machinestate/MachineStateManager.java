package com.example.client.data.machinestate;

import com.example.client.kafka.KafkaDataSender;
import org.springframework.stereotype.Component;

import com.example.client.data.global.AbstractDataManager;
import com.example.client.util.DataType;

@Component
public class MachineStateManager extends AbstractDataManager<MachineState> {

	public MachineStateManager(KafkaDataSender kafkaDataSender) {
		super(kafkaDataSender, DataType.MACHINE_STATE);
	}

	@Override
	protected MachineState createDataInstance(String dataType) {
		return new MachineState(kafkaDataSender, dataType);
	}
}