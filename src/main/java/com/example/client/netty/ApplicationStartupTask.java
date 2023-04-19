package com.example.client.netty;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.client.data.global.AbstractDataManager;
import com.example.client.data.machinestate.MachineStateManager;
import com.example.client.data.sensor.abrasion.AbrasioManager;
import com.example.client.data.sensor.air.AirInKpaManager;
import com.example.client.data.sensor.air.AirOutKpaManager;
import com.example.client.data.sensor.air.AirOutMpaManager;
import com.example.client.data.sensor.load.LoadManager;
import com.example.client.data.sensor.motor.MotorManager;
import com.example.client.data.sensor.vacumm.VacuumManager;
import com.example.client.data.sensor.velocity.VelocityManager;
import com.example.client.data.sensor.water.WaterManager;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationStartupTask implements ApplicationListener<ApplicationReadyEvent> {

	private final Bootstrap bootstrap;
	private final InetSocketAddress inetSocketAddress;

	private final MotorManager motorManager;
	private final VacuumManager vacuumManager;
	private final AirOutKpaManager airOutKpaManager;
	private final AirOutMpaManager airOutMpaManager;
	private final AirInKpaManager airInKpaManager;
	private final WaterManager waterManager;
	private final AbrasioManager abrasioManager;
	private final LoadManager loadManager;
	private final VelocityManager velocityManager;

	private final MachineStateManager machineState;

	private Channel clientChannel;
	private List<AbstractDataManager<?>> managers;


	@PostConstruct
	private void init(){
		managers = Arrays.asList(
			motorManager,
			vacuumManager,
			airOutKpaManager,
			airOutMpaManager,
			airInKpaManager,
			waterManager,
			abrasioManager,
			loadManager,
			velocityManager,
			machineState
		);
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		connect();
	}

	private void connect() {
		// 통신설정

		// 서버에 연결하고, 연결이 완료될 때까지 대기합니다.
		ChannelFuture clientChannelFuture = bootstrap.connect(inetSocketAddress);

		// 연결 상태를 확인하기 위한 ChannelFutureListener 추가합니다.
		clientChannelFuture.addListener((ChannelFutureListener) future -> {

			// 연결 성공
			if (future.isSuccess()) {
				Channel channel = future.channel();
				log.info("Connected to the server successfully.");

				managers.forEach(manager -> manager.sendData(channel));

			}
			// 연결실패
			else {
				log.error("Failed to connect to the server. Cause: ", future.cause());
			}
		});

		// 동기화를 대신에 채널에서 수행합니다.
		clientChannel = clientChannelFuture.channel();
		clientChannel.closeFuture().addListener((ChannelFuture future) -> log.info("Server channel closed."));

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
