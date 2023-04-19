package com.example.client.data.global;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import com.example.client.netty.DataSender;
import com.example.client.util.DataInfo;
import com.example.client.util.DataType;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractDataManager<T extends AbstractData<?>>  {

	protected final DataSender dataSender;
	protected final DataType dataType;
	protected final List<T> dataList = new ArrayList<>();

	public AbstractDataManager(DataSender dataSender, DataType dataType) {
		this.dataSender = dataSender;
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

	public void sendData(Channel channel) {
		for (T data : dataList) {
			data.dataSend(channel);
		}
	}
}
