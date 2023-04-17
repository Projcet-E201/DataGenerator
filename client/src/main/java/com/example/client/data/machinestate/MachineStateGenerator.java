package com.example.client.data.machinestate;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.CharsetUtil;
import lombok.RequiredArgsConstructor;


public class MachineStateGenerator {

	private final ScheduledExecutorService scheduler;
	private final Random random;


	public MachineStateGenerator() {
		this.scheduler = Executors.newScheduledThreadPool(1);
		this.random = new Random();
	}

	public void sendData(Channel channel) {

		scheduler.scheduleAtFixedRate(() -> {
			String combinedData = generateCombinedData();
			System.out.println("Sending data: " + combinedData);

			ByteBuf message = Unpooled.copiedBuffer(combinedData, CharsetUtil.UTF_8);
			ByteBuf delimiter = Unpooled.copiedBuffer("\n", CharsetUtil.UTF_8); // 라인 구분자
			ByteBuf combinedMessage = Unpooled.wrappedBuffer(message, delimiter); // 데이터와 라인 구분자를 결합
			ChannelFuture future = channel.writeAndFlush(combinedMessage);


			future.addListener((ChannelFutureListener) channelFuture -> {
				if (!channelFuture.isSuccess()) {
					System.err.println("Failed to send data. Retrying...");
				} else {
					System.out.println("Data sent successfully.");
					// channel.writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
				}
			});
		}, 0, 1, TimeUnit.SECONDS); // 5초 간격으로 데이터 전송
	}

	private String generateCombinedData() {
		// ... 여기에 모든 데이터 생성 로직 작성
		// 예시: "boolean1:0,boolean2:1,double1:3.14,int1:100,string1:hello"
		StringBuilder combinedData = new StringBuilder();

		// Boolean Type 데이터 생성
		for (int i = 0; i < 10; i++) {
			combinedData.append("boolean").append(i).append(":");
			combinedData.append(random.nextBoolean() ? 1 : 0).append(",");
		}

		// Double Type 데이터 생성
		for (int i = 0; i < 10; i++) {
			combinedData.append("double").append(i).append(":");
			combinedData.append(String.format("%.3f", random.nextDouble() * 310.0 - 10.0)).append(",");
		}

		// Int Type 데이터 생성
		for (int i = 0; i < 10; i++) {
			combinedData.append("int").append(i).append(":");
			combinedData.append(random.nextInt(1101) - 100).append(",");
		}

		// String Type 데이터 생성
		for (int i = 0; i < 10; i++) {
			combinedData.append("string").append(i).append(":");
			String randomString = RandomStringUtils.randomAlphanumeric(random.nextInt(51));
			combinedData.append(randomString).append(",");
		}

		return combinedData.toString();
	}
}
