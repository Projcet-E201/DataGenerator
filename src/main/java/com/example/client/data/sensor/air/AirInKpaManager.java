package com.example.client.data.sensor.air;

import com.example.client.kafka.sender.ChunkDataSender;
import com.example.client.kafka.sender.DataSender;
import org.springframework.stereotype.Component;

import com.example.client.data.global.AbstractDataManager;
import com.example.client.util.DataType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AirInKpaManager extends AbstractDataManager<AirInKpa> {

	public AirInKpaManager(DataSender dataSender, ChunkDataSender chunkDataSender) {
		super(dataSender, chunkDataSender, DataType.AIR_IN_KPA);
	}

	@Override
	protected AirInKpa createDataInstance(String dataType) {
		return new AirInKpa(dataSender, chunkDataSender, dataType);
	}
}