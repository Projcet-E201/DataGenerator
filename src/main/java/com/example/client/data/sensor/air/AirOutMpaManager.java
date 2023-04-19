package com.example.client.data.sensor.air;

import org.springframework.stereotype.Component;

import com.example.client.data.global.AbstractDataManager;
import com.example.client.netty.DataSender;
import com.example.client.util.DataType;

@Component
public class AirOutMpaManager extends AbstractDataManager<AirOutMpa> {

	public AirOutMpaManager(DataSender dataSender) {
		super(dataSender, DataType.AIR_OUT_MPA);
	}

	@Override
	protected AirOutMpa createDataInstance(String dataType) {
		return new AirOutMpa(dataSender, dataType);
	}
}