package com.example.client.data.sensor.air;

import org.springframework.stereotype.Component;

import com.example.client.data.global.AbstractDataManager;
import com.example.client.netty.DataSender;
import com.example.client.util.DataType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AirInKpaManager extends AbstractDataManager<AirInKpa> {

	public AirInKpaManager(DataSender dataSender) {
		super(dataSender, DataType.AIR_IN_KPA);
	}

	@Override
	protected AirInKpa createDataInstance(String dataType) {
		return new AirInKpa(dataSender, dataType);
	}
}