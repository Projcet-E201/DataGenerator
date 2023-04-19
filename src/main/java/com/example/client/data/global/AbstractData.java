package com.example.client.data.global;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.example.client.netty.DataSender;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractData<T> {
	protected final Random random = new Random();
	protected final String dataType;
	protected final DataSender dataSender;
	protected final ConcurrentLinkedQueue<T> dataQueue = new ConcurrentLinkedQueue<>();

	protected final ScheduledExecutorService dataGenerationScheduler = Executors.newScheduledThreadPool(1);
	protected final ScheduledExecutorService sendDataScheduler = Executors.newScheduledThreadPool(1);

	public AbstractData(DataSender dataSender, String dataType) {
		this.dataSender = dataSender;
		this.dataType = dataType;
	}

	public abstract void dataGenerate();
	public abstract void dataSend(Channel channel);
}