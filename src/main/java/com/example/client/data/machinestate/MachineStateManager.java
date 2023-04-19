package com.example.client.data.machinestate;

import org.springframework.stereotype.Component;

import com.example.client.data.global.AbstractDataManager;
import com.example.client.netty.DataSender;
import com.example.client.util.DataType;

@Component
public class MachineStateManager extends AbstractDataManager<MachineState> {

	public MachineStateManager(DataSender dataSender) {
		super(dataSender, DataType.MACHINE_STATE);
	}

	@Override
	protected MachineState createDataInstance(String dataType) {
		return new MachineState(dataSender, dataType);
	}
}