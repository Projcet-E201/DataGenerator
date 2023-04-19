package com.example.client.data.sensor.air;

import org.springframework.stereotype.Component;

import com.example.client.data.global.AbstractDataManager;
import com.example.client.netty.DataSender;
import com.example.client.util.DataType;

@Component
public class AirOutKpaManager extends AbstractDataManager<AirOutKpa> {

	public AirOutKpaManager(DataSender dataSender) {
		super(dataSender, DataType.AIR_OUT_KPA);
	}

	@Override
	protected AirOutKpa createDataInstance(String dataType) {
		return new AirOutKpa(dataSender, dataType);
	}
}