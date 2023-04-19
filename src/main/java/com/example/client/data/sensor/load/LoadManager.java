package com.example.client.data.sensor.load;

import org.springframework.stereotype.Component;

import com.example.client.data.global.AbstractDataManager;
import com.example.client.netty.DataSender;
import com.example.client.util.DataType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LoadManager extends AbstractDataManager<Load> {

	public LoadManager(DataSender dataSender) {
		super(dataSender, DataType.LOAD);
	}

	@Override
	protected Load createDataInstance(String dataType) {
		return new Load(dataSender, dataType);
	}
}