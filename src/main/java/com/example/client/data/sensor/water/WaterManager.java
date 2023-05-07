package com.example.client.data.sensor.water;

import com.example.client.kafka.sender.ChunkDataSender;
import com.example.client.kafka.sender.DataSender;
import org.springframework.stereotype.Component;

import com.example.client.data.global.AbstractDataManager;
import com.example.client.util.DataType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WaterManager extends AbstractDataManager<Water> {

	public WaterManager(DataSender dataSender, ChunkDataSender chunkDataSender, DataSender sensorSender) {
		super(dataSender, chunkDataSender, sensorSender, DataType.WATER);
	}

	@Override
	protected Water createDataInstance(String dataType) {
		return new Water(dataSender, chunkDataSender, dataSender, dataType);
	}
}