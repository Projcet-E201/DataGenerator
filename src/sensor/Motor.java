package sensor;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import config.Config;
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
	private final TcpClient tcpClient;

	public Motor(long id, int generateDataInterval) {
		this.id = id;
		this.generateDataInterval = generateDataInterval;
		this.rand = new Random();
		this.tcpClient = new TcpClient(Config.SERVER_IP, Config.SENSOR_PORT);
	}

	/**
	 * Motor에서 진행하는 함수
	 */
	public void start() {
		ScheduledExecutorService dataGenerator = Executors.newSingleThreadScheduledExecutor();
		dataGenerator.scheduleAtFixedRate(this::motorDataGenerator, 0, generateDataInterval, TimeUnit.SECONDS);
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
		data = Math.max(Math.min(data, 300), -300);

		SensorDataDto sensorDataDto = new SensorDataDto(Sensor.MOTOR, this.id, data);

		int maxRetries = 3;
		int currentAttempt = 0;
		boolean successfulTransmission = false;

		while (!successfulTransmission && currentAttempt < maxRetries) {
			successfulTransmission = tcpClient.sendData(sensorDataDto);
			if (!successfulTransmission) {
				System.err.println("Failed to send data to server (attempt " + (currentAttempt + 1) + ")");
				currentAttempt++;
			}
		}

		if (successfulTransmission) {
			System.out.println("Data sent successfully.");
		} else {
			System.out.println("Failed after " + maxRetries + " attempts.");
		}
	}

	public static void main(String[] args) {
		// Start 10 Motor instances
		for (int i = 0; i < 10; i++) {
			Motor motor = new Motor(i, 1);
			motor.start();
			try {
				Thread.sleep(1); // Add a small delay between starting each motor instance
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
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