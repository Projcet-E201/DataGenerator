package com.example.client.data.sensor.abrasion;

import com.example.client.kafka.sender.ChunkDataSender;
import com.example.client.kafka.sender.DataSender;
import org.springframework.stereotype.Component;

import com.example.client.data.global.AbstractDataManager;
import com.example.client.util.DataType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AbrasioManager extends AbstractDataManager<Abrasio> {

	public AbrasioManager(DataSender dataSender, ChunkDataSender chunkDataSender) {
		super(dataSender, chunkDataSender, DataType.ABRASION);
	}

	@Override
	protected Abrasio createDataInstance(String dataType) {
		return new Abrasio(dataSender, chunkDataSender, dataType);
	}
}