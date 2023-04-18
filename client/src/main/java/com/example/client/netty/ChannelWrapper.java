package com.example.client.netty;

import io.netty.channel.Channel;

public class ChannelWrapper {
	private Channel channel;

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public Channel getChannel() {
		return channel;
	}

	public boolean isChannelInitialized() {
		return channel != null;
	}

}
