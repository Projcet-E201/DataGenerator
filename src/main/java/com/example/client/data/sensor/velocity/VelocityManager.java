package com.example.client.data.sensor.velocity;

import com.example.client.kafka.sender.ChunkDataSender;
import com.example.client.kafka.sender.DataSender;
import com.example.client.kafka.sender.SensorSender;
import org.springframework.stereotype.Component;

import com.example.client.data.global.AbstractDataManager;
import com.example.client.util.DataType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class VelocityManager extends AbstractDataManager<Velocity> {

	public VelocityManager(DataSender dataSender, ChunkDataSender chunkDataSender, SensorSender sensorSender) {
		super(dataSender, chunkDataSender, sensorSender, DataType.VELOCITY);
	}

	@Override
	protected Velocity createDataInstance(String dataType) {
		return new Velocity(dataSender, chunkDataSender, sensorSender, dataType);
	}
}