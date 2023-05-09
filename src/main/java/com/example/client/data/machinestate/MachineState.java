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

			// Boolean Type 데이터 생성
			for (int i = 1; i < 11; i++) {
				StringBuilder data = new StringBuilder();

				data.append("boolean").append(i).append(":");
				data.append(random.nextBoolean() ? 1 : 0);

				dataSender.sendData("MACHINE_STATE", dataType, data.toString());
			}

		}, 0, DataInfo.BOOLEAN_MACHINE_STATE_GENERATE_TIME, DataInfo.BOOLEAN_MACHINE_STATE_GENERATE_TIME_UNIT);

		dataGenerationScheduler.scheduleAtFixedRate(() -> {

			// Double Type 데이터 생성
			for(int i = 1; i < 11; i++) {
				StringBuilder data = new StringBuilder();

				data.append("double").append(i).append(":");
				data.append(String.format("%.3f", random.nextDouble() * 310.0 - 10.0));

				dataSender.sendData("MACHINE_STATE", dataType, data.toString());
			}

		}, 0, DataInfo.DOUBLE_MACHINE_STATE_GENERATE_TIME, DataInfo.DOUBLE_MACHINE_STATE_GENERATE_TIME_UNIT);

		dataGenerationScheduler.scheduleAtFixedRate(() -> {

			// Int Type 데이터 생성
			for (int i = 1; i < 11; i++) {
				StringBuilder data = new StringBuilder();

				data.append("int").append(i).append(":");
				data.append(random.nextInt(1101) - 100);

				dataSender.sendData("MACHINE_STATE", dataType, data.toString());
			}

		}, 0, DataInfo.INT_MACHINE_STATE_GENERATE_TIME, DataInfo.INT_MACHINE_STATE_GENERATE_TIME_UNIT);

		dataGenerationScheduler.scheduleAtFixedRate(() -> {

			// String Type 데이터 생성
			for (int i = 1; i < 11; i++) {
				StringBuilder data = new StringBuilder();
				data.append("string").append(i).append(":");
				String randomString = RandomStringUtils.randomAlphanumeric(1 + random.nextInt(50));
				data.append(randomString);
				dataSender.sendData("MACHINE_STATE", dataType, data.toString());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		}, 0, DataInfo.STRING_MACHINE_STATE_GENERATE_TIME, DataInfo.STRING_MACHINE_STATE_GENERATE_TIME_UNIT);
	}

	@Override
	public void kafkaDataSend() {}
}