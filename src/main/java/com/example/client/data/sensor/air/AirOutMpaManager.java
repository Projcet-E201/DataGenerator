package com.example.client.data.sensor.air;

import com.example.client.kafka.sender.ChunkDataSender;
import com.example.client.kafka.sender.DataSender;
import org.springframework.stereotype.Component;

import com.example.client.data.global.AbstractDataManager;
import com.example.client.util.DataType;

@Component
public class AirOutMpaManager extends AbstractDataManager<AirOutMpa> {

	public AirOutMpaManager(DataSender dataSender, ChunkDataSender chunkDataSender) {
		super(dataSender, chunkDataSender, DataType.AIR_OUT_MPA);
	}

	@Override
	protected AirOutMpa createDataInstance(String dataType) {
		return new AirOutMpa(dataSender, chunkDataSender, dataType);
	}
}