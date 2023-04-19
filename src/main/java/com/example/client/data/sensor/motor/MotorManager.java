package com.example.client.data.sensor.motor;

import org.springframework.stereotype.Component;

import com.example.client.data.global.AbstractDataManager;
import com.example.client.netty.DataSender;
import com.example.client.util.DataType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MotorManager extends AbstractDataManager<Motor> {

	public MotorManager(DataSender dataSender) {
		super(dataSender, DataType.MOTOR);
	}

	@Override
	protected Motor createDataInstance(String dataType) {
		return new Motor(dataSender, dataType);
	}
}