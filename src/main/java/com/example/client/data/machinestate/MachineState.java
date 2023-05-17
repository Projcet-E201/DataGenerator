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
		"반도체는 현대 전자기기의 핵심 부품입니다.",
		"삼성은 세계적인 반도체 제조 기업입니다.",
		"DRAM과 NAND는 대표적인 반도체입니다.",
		"반도체 제조는 고도의 기술이 필요합니다.",
		"미세공정 기술은 반도체의 중요한 요소입니다.",
		"반도체는 컴퓨터의 CPU에도 사용됩니다.",
		"반도체 시장은 빠르게 성장하고 있습니다.",
		"반도체는 스마트폰에도 쓰입니다.",
		"메모리 반도체는 정보를 저장하는 역할을 합니다.",
		"시스템 반도체는 다양한 기능을 수행합니다.",
		"반도체 제조는 청정 공간에서 이루어집니다.",
		"반도체는 우리의 일상 생활을 지배합니다.",
		"반도체 산업은 높은 투자가 필요합니다.",
		"반도체 기술은 계속 발전하고 있습니다.",
		"반도체는 고속 통신을 가능하게 합니다.",
		"반도체는 무선 통신 기기에 필수적입니다.",
		"반도체는 각종 센서의 작동을 돕습니다.",
		"반도체는 차량의 전자 시스템에도 사용됩니다.",
		"반도체는 에너지 효율을 높여줍니다.",
		"반도체는 빅 데이터 처리를 가능하게 합니다."
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

					log.info("data : {}", data);
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