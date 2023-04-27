package com.example.client.data.global;

import com.example.client.kafka.sender.ChunkDataSender;
import com.example.client.kafka.sender.DataSender;
import com.example.client.kafka.sender.SensorSender;
import com.example.client.util.DataInfo;
import com.example.client.util.DataType;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


@Slf4j
public abstract class AbstractDataManager<T extends AbstractData<?>>  {

	protected final DataSender dataSender;
	protected final DataType dataType;
	protected final ChunkDataSender chunkDataSender;
	protected final SensorSender sensorSender;
	protected final List<T> dataList = new ArrayList<>();

	public AbstractDataManager(DataSender dataSender, ChunkDataSender chunkDataSender, SensorSender sensorSender, DataType dataType) {
		this.dataSender = dataSender;
		this.chunkDataSender = chunkDataSender;
		this.sensorSender = sensorSender;
		this.dataType = dataType;
	}

	@PostConstruct
	public void initData() {
		int count = DataInfo.getDataCountByType(dataType);
		for (int i = 1; i <= count; i++) {
			T data = createDataInstance(dataType.toString() + i);
			data.dataGenerate();
			dataList.add(data);
		}
	}

	protected abstract T createDataInstance(String dataType);

	public void sendData() {
		for (T data : dataList) {
			data.kafkaDataSend();
		}
	}
}
