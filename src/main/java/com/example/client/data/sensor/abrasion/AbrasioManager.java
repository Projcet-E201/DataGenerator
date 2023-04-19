package com.example.client.data.sensor.abrasion;

import org.springframework.stereotype.Component;

import com.example.client.data.global.AbstractDataManager;
import com.example.client.netty.DataSender;
import com.example.client.util.DataType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AbrasioManager extends AbstractDataManager<Abrasio> {

	public AbrasioManager(DataSender dataSender) {
		super(dataSender, DataType.ABRASION);
	}

	@Override
	protected Abrasio createDataInstance(String dataType) {
		return new Abrasio(dataSender, dataType);
	}
}