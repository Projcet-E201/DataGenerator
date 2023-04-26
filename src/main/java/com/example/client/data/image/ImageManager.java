package com.example.client.data.image;

import com.example.client.kafka.KafkaDataSender;
import org.springframework.stereotype.Component;

import com.example.client.data.global.AbstractDataManager;
import com.example.client.util.DataType;

@Component
public class ImageManager extends AbstractDataManager<Image> {

	public ImageManager(KafkaDataSender kafkaDataSender) {
		super(kafkaDataSender,DataType.IMAGE);
	}

	@Override
	protected Image createDataInstance(String dataType) {
		return new Image(kafkaDataSender, dataType);
	}
}