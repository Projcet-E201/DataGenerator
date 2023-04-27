package com.example.client.data.sensor.load;

import com.example.client.kafka.sender.ChunkDataSender;
import com.example.client.kafka.sender.DataSender;
import com.example.client.kafka.sender.SensorSender;
import org.springframework.stereotype.Component;

import com.example.client.data.global.AbstractDataManager;
import com.example.client.util.DataType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LoadManager extends AbstractDataManager<Load> {

	public LoadManager(DataSender dataSender, ChunkDataSender chunkDataSender, SensorSender sensorSender) {
		super(dataSender, chunkDataSender, sensorSender, DataType.LOAD);
	}

	@Override
	protected Load createDataInstance(String dataType) {
		return new Load(dataSender, chunkDataSender, sensorSender, dataType);
	}
}