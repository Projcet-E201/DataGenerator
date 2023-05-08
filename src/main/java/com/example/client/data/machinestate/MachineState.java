package com.example.client.data.machinestate;

import com.example.client.kafka.sender.ChunkDataSender;
import com.example.client.kafka.sender.DataSender;
import org.apache.commons.lang3.RandomStringUtils;

import com.example.client.data.global.AbstractData;
import com.example.client.util.DataInfo;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class MachineState extends AbstractData<String> {

	public MachineState(DataSender dataSender, ChunkDataSender chunkDataSender, String dataType) {
		super(dataSender, chunkDataSender, dataType);
	}

	@Override
	public void dataGenerate() {
		dataGenerationScheduler.scheduleAtFixedRate(() -> {
			StringBuilder data = new StringBuilder();

			// Boolean Type 데이터 생성
			data.append("boolean").append(random.nextInt(11) + 1).append(":");
			data.append(random.nextBoolean() ? 1 : 0);

			dataSender.sendData("MACHINE_STATE", dataType, data.toString());
		}, 0, DataInfo.BOOLEAN_MACHINE_STATE_GENERATE_TIME, DataInfo.BOOLEAN_MACHINE_STATE_GENERATE_TIME_UNIT);

		dataGenerationScheduler.scheduleAtFixedRate(() -> {
			StringBuilder data = new StringBuilder();

			// Double Type 데이터 생성
			data.append("double").append(random.nextInt(11) + 1).append(":");
			data.append(String.format("%.3f", random.nextDouble() * 310.0 - 10.0));

			dataSender.sendData("MACHINE_STATE", dataType, data.toString());
		}, 0, DataInfo.DOUBLE_MACHINE_STATE_GENERATE_TIME, DataInfo.DOUBLE_MACHINE_STATE_GENERATE_TIME_UNIT);

		dataGenerationScheduler.scheduleAtFixedRate(() -> {
			StringBuilder data = new StringBuilder();

			// Int Type 데이터 생성
			data.append("int").append(random.nextInt(11) + 1).append(":");
			data.append(random.nextInt(1101) - 100);

			dataSender.sendData("MACHINE_STATE", dataType, data.toString());
		}, 0, DataInfo.INT_MACHINE_STATE_GENERATE_TIME, DataInfo.INT_MACHINE_STATE_GENERATE_TIME_UNIT);

		dataGenerationScheduler.scheduleAtFixedRate(() -> {
			StringBuilder data = new StringBuilder();

			// String Type 데이터 생성
			data.append("string").append(random.nextInt(11) + 1).append(":");
			String randomString = RandomStringUtils.randomAlphanumeric(1 + random.nextInt(50));
			data.append(randomString);

			dataSender.sendData("MACHINE_STATE", dataType, data.toString());
		}, 0, DataInfo.STRING_MACHINE_STATE_GENERATE_TIME, DataInfo.STRING_MACHINE_STATE_GENERATE_TIME_UNIT);
	}

	@Override
	public void kafkaDataSend() {}
}