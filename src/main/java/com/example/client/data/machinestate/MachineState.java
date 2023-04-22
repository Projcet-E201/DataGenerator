package com.example.client.data.machinestate;

import org.apache.commons.lang3.RandomStringUtils;

import com.example.client.data.global.AbstractData;
import com.example.client.netty.DataSender;
import com.example.client.util.DataInfo;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class MachineState extends AbstractData<String> {

	public MachineState(DataSender dataSender, String dataType) {
		super(dataSender, dataType);
	}

	@Override
	public void dataGenerate() {
		dataGenerationScheduler.scheduleAtFixedRate(() -> {
			// MachineState 데이터 생성 로직
			StringBuilder data = new StringBuilder();

			// Boolean Type 데이터 생성
			for (int i = 1; i <= 10; i++) {
				data.append("boolean").append(i).append(":");
				data.append(random.nextBoolean() ? 1 : 0).append(",");
			}

			// Double Type 데이터 생성
			for (int i = 1; i <= 10; i++) {
				data.append("double").append(i).append(":");
				data.append(String.format("%.3f", random.nextDouble() * 310.0 - 10.0)).append(",");
			}

			// Int Type 데이터 생성
			for (int i = 1; i <= 10; i++) {
				data.append("int").append(i).append(":");
				data.append(random.nextInt(1101) - 100).append(",");
			}

			// String Type 데이터 생성
			for (int i = 1; i <= 10; i++) {
				data.append("string").append(i).append(":");
				String randomString = RandomStringUtils.randomAlphanumeric(random.nextInt(51));
				data.append(randomString).append(",");
			}

			dataQueue.offer(data.toString());
		}, 0, DataInfo.MACHINE_STATE_GENERATE_TIME, DataInfo.MACHINE_STATE_GENERATE_TIME_UNIT);
	}

	@Override
	public void dataSend(Channel channel) {
		sendDataScheduler.scheduleAtFixedRate(() -> {
			String data = dataQueue.poll();
			if (data != null) {
				dataSender.sendData(channel, dataType, data);
			}
		}, DataInfo.MACHINE_STATE_CALCULATE_TIME, DataInfo.MACHINE_STATE_CALCULATE_TIME, DataInfo.MACHINE_STATE_CALCULATE_TIME_UNIT);
	}
}
