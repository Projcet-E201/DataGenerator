package com.example.client.data.sensor.vacumm;

import org.springframework.stereotype.Component;

import com.example.client.data.global.AbstractDataManager;
import com.example.client.netty.DataSender;
import com.example.client.util.DataType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class VacuumManager extends AbstractDataManager<Vacuum> {

	public VacuumManager(DataSender dataSender) {
		super(dataSender, DataType.VACUUM);
	}

	@Override
	protected Vacuum createDataInstance(String dataType) {
		return new Vacuum(dataSender, dataType);
	}
}
