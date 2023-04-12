package sensor;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import config.Config;
import config.Sensor;

/**
 * 개수 - 5
 * 범위 - 0~900
 * 생성주기 - 5ms
 * 가공구간 - 10s
 * 단위 - kPa
 */
public class Air1 {
	// 서버 정보
	private static final String ip = Config.IP;
	private static final int port = Config.PORT;

	// 데이터 정보
	private static final List<Integer> dataList = new ArrayList<>();
	private static final int sendDataSize = 200; // 해당 개수가 생성되면 데이터를 보낸다.
	private static final int generateDataInterval = 5; // 5ms
	private static final int retries= 3; // 재시도 횟수

	public static void main(String[] args) {
		motorDataGeneratorAndSender();
	}



	/**
	 * 데이터를 5ms 마다 생성한다.
	 */
	private static void motorDataGeneratorAndSender() {
		Random rand = new Random();

		// Generate data every 5 ms
		ScheduledExecutorService motor1 = Executors.newSingleThreadScheduledExecutor();
		motor1.scheduleAtFixedRate(() -> {
			int data = rand.nextInt(900);
			dataList.add(data);

			if (dataList.size() >= sendDataSize) {
				sendDataToServer();
			}

		}, 0, generateDataInterval, TimeUnit.MILLISECONDS);


	}

	/**
	 * Data를 Socket으로 전송하는 함수
	 */
	private static void sendDataToServer() {
		int maxRetries = retries; // 재시도 횟수
		int currentAttempt = 0;
		boolean successfulTransmission = false;
		Socket socket = null;


		List<Integer> dataToSend;
		synchronized (dataList) {
			dataToSend = new ArrayList<>(dataList);
			dataList.clear();
		}

		while (!successfulTransmission && currentAttempt < maxRetries) {
			try {
				// Socket 연결
				socket = new Socket(ip, port);

				// 데이터를 전송할 outputStream 생성
				OutputStream outputStream = socket.getOutputStream();
				System.out.println(dataToSend.size());

				outputStream.write((Sensor.AIR1 + " ").getBytes());

				// data를 집어넣음
				for (Integer data : dataToSend) {
					outputStream.write((data + " ").getBytes());
				}
				// data 전송
				outputStream.close();

				successfulTransmission = true;

			}
			// 만약 전송에 문제가 생긴경우 (최대 3번 다시 보낸다.)
			catch (IOException e) {
				System.err.println("Failed to send data to server (attempt " + (currentAttempt + 1) + "): " + e.getMessage());
				currentAttempt++;
			}
			// 전송이 끝나면 socket을 무조건 닫아준다.
			finally {
				if (socket != null) {
					try {
						socket.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}

		if (successfulTransmission) {
			System.out.println("Data sent successfully.");
		} else {
			System.out.println("Failed after " + maxRetries + " attempts.");
		}
	}
}
