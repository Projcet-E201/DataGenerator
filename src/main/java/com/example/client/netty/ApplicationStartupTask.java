package com.example.client.netty;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.example.client.netty.analog.AnalogNettySocket;
import com.example.client.netty.data.DataNettySocket;
import com.example.client.netty.image.ImageNettySocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationStartupTask implements ApplicationListener<ApplicationReadyEvent> {

	private final DataNettySocket dataNettySocket;
	private final AnalogNettySocket analogNettySocket;
	private final ImageNettySocket imageNettySocket;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		dataNettySocket.connect();
		analogNettySocket.connect();
		imageNettySocket.connect();
	}

}
