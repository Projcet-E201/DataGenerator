package com.example.client.data.sensor.motor;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import com.example.client.netty.DataSender;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Motor {

	private final Random random = new Random();
	private final String dataType;
	private final DataSender dataSender;


	// 생성자 수정
	public Motor(DataSender dataSender, String dataType) {
		this.dataSender = dataSender;
		this.dataType = dataType;
		init();
	}
	private final ConcurrentLinkedQueue<Integer> dataQueue = new ConcurrentLinkedQueue<>();


	// 스케줄러 전역 변수로 빼기
	private final ScheduledExecutorService dataGenerationScheduler = Executors.newScheduledThreadPool(1);
	private final ScheduledExecutorService sendDataScheduler = Executors.newScheduledThreadPool(1);

	@PostConstruct
	public void init() {
		// 주기적으로 데이터를 생성하는 코드
		dataGenerationScheduler.scheduleAtFixedRate(() -> {
			Integer data = random.nextInt(601) - 300;
			dataQueue.offer(data);
		}, 0, 5, TimeUnit.MILLISECONDS);
	}

	public void dataSend(Channel channel) {
		// 5초마다 생성된 데이터 중 최대값을 찾는 코드
		sendDataScheduler.scheduleAtFixedRate(() -> {
			int maxData = Integer.MIN_VALUE;
			Integer data;
			while ((data = dataQueue.poll()) != null) {
				maxData = Math.max(maxData, data);
			}

			dataSender.sendData(channel, dataType, maxData);
		}, 5, 5, TimeUnit.SECONDS);
	}
}
