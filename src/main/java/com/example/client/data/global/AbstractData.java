package com.example.client.data.global;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.example.client.kafka.sender.ChunkDataSender;

import com.example.client.kafka.sender.DataSender;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractData<T> {

	protected final DataSender dataSender;
	protected final ChunkDataSender chunkDataSender;

	protected final String dataType;
	protected final Random random = new Random();
	protected final ConcurrentLinkedQueue<T> dataQueue = new ConcurrentLinkedQueue<>();

	protected final ScheduledExecutorService dataGenerationScheduler = Executors.newScheduledThreadPool(2);
	protected final ScheduledExecutorService sendDataScheduler = Executors.newScheduledThreadPool(2);

	public AbstractData(DataSender dataSender, ChunkDataSender chunkDataSender, String dataType) {
		this.chunkDataSender = chunkDataSender;
		this.dataSender = dataSender;
		this.dataType = dataType;
	}

	public abstract void dataGenerate();
	public abstract void kafkaDataSend();
}