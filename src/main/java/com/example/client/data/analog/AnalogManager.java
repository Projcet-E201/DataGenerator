package com.example.client.data.analog;

import com.example.client.kafka.sender.ChunkDataSender;
import com.example.client.kafka.sender.DataSender;
import com.example.client.kafka.sender.SensorSender;
import org.springframework.stereotype.Component;

import com.example.client.data.global.AbstractDataManager;
import com.example.client.util.DataType;

@Component
public class AnalogManager extends AbstractDataManager<Analog> {

	public AnalogManager(DataSender dataSender, ChunkDataSender chunkDataSender, SensorSender sensorSender) {
		super(dataSender, chunkDataSender, sensorSender, DataType.ANALOG);
	}

	@Override
	protected Analog createDataInstance(String dataType) {
		return new Analog(dataSender, chunkDataSender, sensorSender, dataType);
	}
}
