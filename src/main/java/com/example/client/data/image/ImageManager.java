package com.example.client.data.image;

import org.springframework.stereotype.Component;

import com.example.client.data.global.AbstractDataManager;
import com.example.client.netty.DataSender;
import com.example.client.util.DataType;

@Component
public class ImageManager extends AbstractDataManager<Image> {

	public ImageManager(DataSender dataSender) {
		super(dataSender, DataType.IMAGE);
	}

	@Override
	protected Image createDataInstance(String dataType) {
		return new Image(dataSender, dataType);
	}
}