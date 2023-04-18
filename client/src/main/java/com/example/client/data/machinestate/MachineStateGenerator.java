package com.example.client.data.machinestate;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import com.example.client.netty.DataSender;

import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MachineStateGenerator {

	private static final int generateDataInterval = 5; // 5s
	private static final int sendDataInterval = 5; // 5s
	private static final String dataType = "MachineState";

	private final Random random = new Random();
	private final DataSender dataSender;
	private final ScheduledExecutorService sendDataScheduler = Executors.newScheduledThreadPool(1);

	private final AtomicReference<String> generatedData = new AtomicReference<>();

	@PostConstruct
	public void init() {
		// 주기적으로 데이터를 생성하는 코드
		ScheduledExecutorService dataGenerationScheduler = Executors.newScheduledThreadPool(1);
		dataGenerationScheduler.scheduleAtFixedRate(() -> {
			// ... 여기에 모든 데이터 생성 로직 작성
			// 예시: "boolean1:0,boolean2:1,double1:3.14,int1:100,string1:hello"
			StringBuilder data = new StringBuilder();

			// Boolean Type 데이터 생성
			for (int i = 0; i < 10; i++) {
				data.append("boolean").append(i).append(":");
				data.append(random.nextBoolean() ? 1 : 0).append(",");
			}

			// Double Type 데이터 생성
			for (int i = 0; i < 10; i++) {
				data.append("double").append(i).append(":");
				data.append(String.format("%.3f", random.nextDouble() * 310.0 - 10.0)).append(",");
			}

			// Int Type 데이터 생성
			for (int i = 0; i < 10; i++) {
				data.append("int").append(i).append(":");
				data.append(random.nextInt(1101) - 100).append(",");
			}

			// String Type 데이터 생성
			for (int i = 0; i < 10; i++) {
				data.append("string").append(i).append(":");
				String randomString = RandomStringUtils.randomAlphanumeric(random.nextInt(51));
				data.append(randomString).append(",");
			}

			generatedData.set(data.toString());
		}, 0, generateDataInterval, TimeUnit.SECONDS);
	}

	public void sendData(Channel channel) {
		// 5초마다 생성된 데이터 중 최대값을 찾는 코드
		sendDataScheduler.scheduleAtFixedRate(() -> {
			String data = generatedData.get();
			dataSender.sendData(channel, dataType, data);
		}, 1, 1, TimeUnit.SECONDS);
	}
}
