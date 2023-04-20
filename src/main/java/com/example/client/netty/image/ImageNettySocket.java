package com.example.client.netty.image;

import java.net.InetSocketAddress;
import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.example.client.data.image.ImageManager;
import com.example.client.netty.global.socket.AbstractNettySocket;

import io.netty.bootstrap.Bootstrap;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ImageNettySocket extends AbstractNettySocket {

	private final ImageManager imageManager;

	public ImageNettySocket(
		@Qualifier("imageServerBootstrap") Bootstrap bootstrap,
		@Qualifier("imageInitSocketAddress") InetSocketAddress tcpPort,
		ImageManager imageManager) {

		super(bootstrap, tcpPort);
		this.imageManager = imageManager;
	}

	@PostConstruct
	private void init() {
		managers = Arrays.asList(
			imageManager
		);
	}

}
