package sensor;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import config.Sensor;

/**
 * 개수 - 10
 * 범위 - -300~300 (음수 ABS처리 )
 * 생성주기 - 5ms
 * 가공구간 - 5s
 */
public class Motor {

	private final long id;
	private final int generateDataInterval;
	private final Random rand;

	public Motor(long id, int generateDataInterval) {
		this.id = id;
		this.generateDataInterval = generateDataInterval;
		this.rand = new Random();
	}

	/**
	 * Motor에서 진행하는 함수
	 */
	public void start() {
		ScheduledExecutorService dataGenerator = Executors.newSingleThreadScheduledExecutor();
		dataGenerator.scheduleAtFixedRate(this::motorDataGenerator, 0, generateDataInterval, TimeUnit.MILLISECONDS);
	}

	/**
	 * 데이터를 5ms 마다 생성한다.
	 */
	private void motorDataGenerator() {
		// 데이터의 노이즈와 변동성을 추가하기 위해, 표준 편차가 있는 가우시간 분포 사용
		double mean = 0;
		double stdDev = 0.1;

		// Generate data every 5 ms
		int data = (int) Math.round(rand.nextGaussian() * stdDev * 600 + mean);
		System.out.println(data);
		data = Math.max(Math.min(data, 300), -300);

		SensorDataDto sensorDataDto = new SensorDataDto(Sensor.MOTOR, this.id, data);
		TcpServerSend.sendDataToServer(sensorDataDto);
	}

	public static void main(String[] args) {
		// Start 10 Motor instances
		for (int i = 0; i < 10; i++) {
			Motor motor = new Motor(i, 5);
			motor.start();
		}

		// Keep the application running
		while (true) {
			try {
				Thread.sleep(1000); // Sleep for 1 second
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}