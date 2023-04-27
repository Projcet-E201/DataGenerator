package com.example.client.data.image;

import com.example.client.kafka.sender.ChunkDataSender;
import com.example.client.kafka.sender.DataSender;
import com.example.client.kafka.sender.SensorSender;
import org.springframework.stereotype.Component;

import com.example.client.data.global.AbstractDataManager;
import com.example.client.util.DataType;

@Component
public class ImageManager extends AbstractDataManager<Image> {

	public ImageManager(DataSender dataSender, ChunkDataSender chunkDataSender, SensorSender sensorSender) {
		super(dataSender, chunkDataSender, sensorSender, DataType.IMAGE);
	}

	@Override
	protected Image createDataInstance(String dataType) {
		return new Image(dataSender, chunkDataSender, sensorSender, dataType);
	}
}