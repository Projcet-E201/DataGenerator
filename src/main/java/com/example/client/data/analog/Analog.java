package com.example.client.data.analog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.example.client.data.global.AbstractData;
import com.example.client.kafka.sender.ChunkDataSender;
import com.example.client.kafka.sender.DataSender;
import com.example.client.util.DataInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Analog extends AbstractData<String> {

	private final byte[] randomBinaryData;

	public Analog(DataSender dataSender, ChunkDataSender chunkDataSender, String dataType) {
		super(dataSender, chunkDataSender, dataType);
		randomBinaryData = new byte[400 * 1024]; // 400KB
	}

	@Override
	public void dataGenerate() {
		dataGenerationScheduler.scheduleAtFixedRate(() -> {
			new Random().nextBytes(randomBinaryData);
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ZipOutputStream zos = new ZipOutputStream(baos);
				zos.putNextEntry(new ZipEntry("analog_data.bin"));
				zos.write(randomBinaryData);
				zos.closeEntry();
				zos.close();

				String encoded = Base64.getEncoder().encodeToString(baos.toByteArray());
				dataQueue.offer(encoded);
			} catch (IOException e) {
				log.error("Error generating analog data", e);
			}
		}, 0, DataInfo.ANALOG_GENERATE_TIME, DataInfo.ANALOG_GENERATE_TIME_UNIT);
	}

	@Override
	public void kafkaDataSend() {
		sendDataScheduler.scheduleAtFixedRate(() -> {
			String data = dataQueue.poll();
			if (data != null) {
				String dataConvent = data + "|";
				dataSender.sendData("ANALOG", dataType, dataConvent);
			}
		}, DataInfo.ANALOG_CALCULATE_TIME, DataInfo.ANALOG_CALCULATE_TIME, DataInfo.ANALOG_CALCULATE_TIME_UNIT);
	}
}
