package com.example.client.data.machinestate;

import com.example.client.kafka.sender.ChunkDataSender;
import com.example.client.kafka.sender.DataSender;
import com.example.client.kafka.sender.SensorSender;
import org.springframework.stereotype.Component;

import com.example.client.data.global.AbstractDataManager;
import com.example.client.util.DataType;

@Component
public class MachineStateManager extends AbstractDataManager<MachineState> {

	public MachineStateManager(DataSender dataSender, ChunkDataSender chunkDataSender, SensorSender sensorSender) {
		super(dataSender, chunkDataSender, sensorSender, DataType.MACHINE_STATE);
	}

	@Override
	protected MachineState createDataInstance(String dataType) {
		return new MachineState(dataSender, chunkDataSender, sensorSender, dataType);
	}
}