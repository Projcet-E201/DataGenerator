package com.example.client.netty;

import java.net.InetSocketAddress;

import javax.annotation.PreDestroy;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.example.client.data.machinestate.MachineStateGenerator;
import com.example.client.data.sensor.motor.MotorManager;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApplicationStartupTask implements ApplicationListener<ApplicationReadyEvent> {

	private final Bootstrap bootstrap;
	private final InetSocketAddress inetSocketAddress;
	private final MotorManager motorManager;
	private final MachineStateGenerator machineStateGenerator;

	private Channel clientChannel;


	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		connect();
	}

	private void connect() {
		// 통신설정

		// 서버에 연결하고, 연결이 완료될 때까지 대기합니다.
		ChannelFuture clientChannelFuture = bootstrap.connect(inetSocketAddress);

		// 연결 상태를 확인하기 위한 ChannelFutureListener 추가합니다.
		clientChannelFuture.addListener((ChannelFutureListener)future -> {

			// 연결 성공
			if (future.isSuccess()) {
				Channel channel = future.channel();
				System.out.println("Connected to the server successfully.");

				// Sensor 생성하고 보냄
				motorManager.sendData(channel);

				// MachineStateGenerator 생성하고 보냄
				machineStateGenerator.sendData(channel);

			}
			// 연결실패
			else {
				System.err.println("Failed to connect to the server. Cause: " + future.cause());
			}
		});

		// 동기화를 대신에 채널에서 수행합니다.
		clientChannel = clientChannelFuture.channel();
		clientChannel.closeFuture().addListener((ChannelFuture future) -> {
			System.out.println("Server channel closed.");
		});

	}

	// Bean을 제거하기 전에 해야할 작업이 있을 때 설정
	@PreDestroy
	public void stop() {
		if (clientChannel != null) {
			clientChannel.close();
			clientChannel.parent().closeFuture();
		}
	}
}
