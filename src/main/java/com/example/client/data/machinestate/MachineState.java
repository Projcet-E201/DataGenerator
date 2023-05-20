package com.example.client.data.machinestate;

import java.util.Arrays;
import java.util.List;

import com.example.client.kafka.sender.ChunkDataSender;
import com.example.client.kafka.sender.DataSender;
import org.apache.commons.lang3.RandomStringUtils;

import com.example.client.data.global.AbstractData;
import com.example.client.util.DataInfo;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class MachineState extends AbstractData<String> {
	public MachineState(DataSender dataSender, ChunkDataSender chunkDataSender, String dataType) {
		super(dataSender, chunkDataSender, dataType);
	}

	List<String> sentences = Arrays.asList(
		"반도체는_현대_전자기기의_핵심_부품입니다.",
		"삼성은_세계적인_반도체_제조_기업입니다.",
		"DRAM과_NAND는_대표적인_반도체입니다.",
		"반도체_제조는_고도의_기술이_필요합니다.",
		"미세공정_기술은_반도체의_중요한_요소입니다.",
		"반도체는_컴퓨터의_CPU에도_사용됩니다.",
		"반도체_시장은_빠르게_성장하고_있습니다.",
		"반도체는_스마트폰에도_쓰입니다.",
		"메모리_반도체는_정보를_저장하는_역할을_합니다.",
		"시스템_반도체는_다양한_기능을_수행합니다.",
		"반도체_제조는_청정_공간에서_이루어집니다.",
		"반도체는_우리의_일상_생활을_지배합니다.",
		"반도체_산업은_높은_투자가_필요합니다.",
		"반도체_기술은_계속_발전하고_있습니다.",
		"반도체는_고속_통신을_가능하게_합니다.",
		"반도체는_무선_통신_기기에_필수적입니다.",
		"반도체는_각종_센서의_작동을_돕습니다.",
		"반도체는_차량의_전자_시스템에도_사용됩니다.",
		"반도체는_에너지_효율을_높여줍니다.",
		"반도체는_빅_데이터_처리를_가능하게_합니다."
	);

	@Override
	public void dataGenerate() {
		dataGenerationScheduler.scheduleAtFixedRate(() -> {

			// Boolean Type 데이터 생성
			for (int i = 1; i < 11; i++) {
				StringBuilder data = new StringBuilder();

				data.append("boolean").append(i).append(":");
				data.append(random.nextBoolean() ? 1 : 0);

				dataSender.sendData("MACHINE_STATE", dataType, data.toString());
			}

		}, 0, DataInfo.BOOLEAN_MACHINE_STATE_GENERATE_TIME, DataInfo.BOOLEAN_MACHINE_STATE_GENERATE_TIME_UNIT);

		dataGenerationScheduler.scheduleAtFixedRate(() -> {

			// Double Type 데이터 생성
			for(int i = 1; i < 11; i++) {
				StringBuilder data = new StringBuilder();

				data.append("double").append(i).append(":");
				data.append(String.format("%.3f", random.nextDouble() * 310.0 - 10.0));

				dataSender.sendData("MACHINE_STATE", dataType, data.toString());
			}

		}, 0, DataInfo.DOUBLE_MACHINE_STATE_GENERATE_TIME, DataInfo.DOUBLE_MACHINE_STATE_GENERATE_TIME_UNIT);

		dataGenerationScheduler.scheduleAtFixedRate(() -> {

			// Int Type 데이터 생성
			for (int i = 1; i < 11; i++) {
				StringBuilder data = new StringBuilder();
				data.append("int").append(i).append(":");
				data.append(random.nextInt(1101) - 100);

				dataSender.sendData("MACHINE_STATE", dataType, data.toString());
			}

		}, 0, DataInfo.INT_MACHINE_STATE_GENERATE_TIME, DataInfo.INT_MACHINE_STATE_GENERATE_TIME_UNIT);

		dataGenerationScheduler.scheduleAtFixedRate(() -> {

			// String Type 데이터 생성
			for (int i = 1; i < 11; i++) {
				StringBuilder data = new StringBuilder();
				data.append("string").append(i).append(":");

				String randomString = sentences.get(random.nextInt(sentences.size()));
				data.append(randomString);
				dataSender.sendData("MACHINE_STATE", dataType, data.toString());
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		}, 0, DataInfo.STRING_MACHINE_STATE_GENERATE_TIME, DataInfo.STRING_MACHINE_STATE_GENERATE_TIME_UNIT);
	}

	@Override
	public void kafkaDataSend() {}
}