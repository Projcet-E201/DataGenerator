package com.example.client.data.analog;

import org.springframework.stereotype.Component;

import com.example.client.data.global.AbstractDataManager;
import com.example.client.netty.DataSender;
import com.example.client.util.DataType;

@Component
public class AnalogManager extends AbstractDataManager<Analog> {

	public AnalogManager(DataSender dataSender) {
		super(dataSender, DataType.ANALOG);
	}

	@Override
	protected Analog createDataInstance(String dataType) {
		return new Analog(dataSender, dataType);
	}
}
