package com.example.client.data.sensor.velocity;

import org.springframework.stereotype.Component;

import com.example.client.data.global.AbstractDataManager;
import com.example.client.netty.DataSender;
import com.example.client.util.DataType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class VelocityManager extends AbstractDataManager<Velocity> {

	public VelocityManager(DataSender dataSender) {
		super(dataSender, DataType.VELOCITY);
	}

	@Override
	protected Velocity createDataInstance(String dataType) {
		return new Velocity(dataSender, dataType);
	}
}