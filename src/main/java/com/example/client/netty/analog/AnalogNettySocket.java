package com.example.client.netty.analog;

import java.net.InetSocketAddress;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.example.client.data.analog.AnalogManager;
import com.example.client.netty.global.socket.AbstractNettySocket;

import io.netty.bootstrap.Bootstrap;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AnalogNettySocket extends AbstractNettySocket {

	private final AnalogManager analogManager;

	public AnalogNettySocket(
		@Qualifier("analogServerBootstrap") Bootstrap bootstrap,
		@Qualifier("analogInitSocketAddress") InetSocketAddress tcpPort,
		AnalogManager analogManager) {

		super(bootstrap, tcpPort);
		this.analogManager = analogManager;
	}

	@PostConstruct
	private void init() {
		managers = Arrays.asList(
			analogManager
		);
	}

}
