package com.example.client.data.sensor.motor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.client.netty.DataSender;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MotorManager {
	// 데이터 정보

	private static final int count = 10; // 개수
	private static final int generateDataInterval = 5; // 5ms
	private static final int sendDataInterval = 5; // 5s
	private static final String dataType = "MOTOR";

	private final DataSender dataSender;
	private final List<Motor> motors = new ArrayList<>();

	public MotorManager(DataSender dataSender) {
		this.dataSender = dataSender;
		initMotors();
	}

	private void initMotors() {
		for (int i = 1; i <= 10; i++) {
			Motor motor = new Motor(dataSender, "Motor" + i);
			motors.add(motor);
		}
	}

	public void sendData(Channel channel) {
		for (Motor motor : motors) {
			motor.dataSend(channel);
		}
	}
}