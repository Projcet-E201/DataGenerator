package com.example.client.netty;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.example.client.data.machinestate.MachineStateGenerator;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApplicationStartupTask implements ApplicationListener<ApplicationReadyEvent> {

	private final Bootstrap bootstrap;
	private final InetSocketAddress inetSocketAddress;
	private final EventLoopGroup eventLoopGroup;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		connect();
	}

	private void connect() {
		// 통신설정
		try {
			// 서버에 연결하고, 연결이 완료될 때까지 대기합니다.
			ChannelFuture connectFuture = bootstrap.connect(inetSocketAddress);

			// 연결 상태를 확인하기 위한 ChannelFutureListener 추가합니다.
			connectFuture.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {

					// 연결 성공
					if (future.isSuccess()) {
						Channel channel = future.channel();
						System.out.println("Connected to the server successfully.");

						// MachineStateGenerator 생성하고 데이터 생성을 시작합니다.
						MachineStateGenerator machineStateGenerator = new MachineStateGenerator();
						machineStateGenerator.sendData(channel);
					}
					// 연결실패
					else {
						System.err.println("Failed to connect to the server. Cause: " + future.cause());
					}
				}
			});

			// 동기화를 대신에 채널에서 수행합니다.
			connectFuture.sync();


		} catch (InterruptedException e) {
			System.out.println("연결실패");
			e.printStackTrace();
		}
	}

}
