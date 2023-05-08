package com.example.client.data.sensor.vacumm;

import com.example.client.kafka.sender.ChunkDataSender;
import com.example.client.kafka.sender.DataSender;
import org.springframework.stereotype.Component;

import com.example.client.data.global.AbstractDataManager;
import com.example.client.util.DataType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class VacuumManager extends AbstractDataManager<Vacuum> {

	public VacuumManager(DataSender dataSender, ChunkDataSender chunkDataSender) {
		super(dataSender, chunkDataSender, DataType.VACUUM);
	}

	@Override
	protected Vacuum createDataInstance(String dataType) {
		return new Vacuum(dataSender, chunkDataSender, dataType);
	}
}
