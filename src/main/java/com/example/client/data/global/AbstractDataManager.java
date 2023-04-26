package com.example.client.data.global;

import com.example.client.kafka.KafkaDataSender;
import com.example.client.util.DataInfo;
import com.example.client.util.DataType;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;


@Slf4j
public abstract class AbstractDataManager<T extends AbstractData<?>>  {

	protected final KafkaDataSender kafkaDataSender;
	protected final DataType dataType;
	protected final List<T> dataList = new ArrayList<>();

	public AbstractDataManager(KafkaDataSender kafkaDataSender, DataType dataType) {
		this.kafkaDataSender = kafkaDataSender;
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
