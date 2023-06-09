package com.example.client.data.sensor.motor;

import com.example.client.kafka.sender.ChunkDataSender;
import com.example.client.kafka.sender.DataSender;
import org.springframework.stereotype.Component;

import com.example.client.data.global.AbstractDataManager;
import com.example.client.util.DataType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MotorManager extends AbstractDataManager<Motor> {

	public MotorManager(DataSender dataSender, ChunkDataSender chunkDataSender) {
		super(dataSender, chunkDataSender, DataType.MOTOR);
	}

	@Override
	protected Motor createDataInstance(String dataType) {
		return new Motor(dataSender, chunkDataSender, dataType);
	}
}