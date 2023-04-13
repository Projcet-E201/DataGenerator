package sensor;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import config.Config;
import config.Sensor;

public class TcpServerSend {

	// 서버 정보
	private static final String ip = Config.SERVER_IP;
	private static final int port = Config.SENSOR_PORT;

	/**
	 * Data를 Socket으로 전송하는 함수
	 */
	public static void sendDataToServer(SensorDataDto sensorDataDto) {
		int maxRetries = 3; // 재시도 횟수
		int currentAttempt = 0;
		boolean successfulTransmission = false;
		Socket socket = null;

		while (!successfulTransmission && currentAttempt < maxRetries) {
			try {
				// Socket 연결
				socket = new Socket(ip, port);

				// 데이터를 전송할 outputStream 생성
				OutputStream outputStream = socket.getOutputStream();
				outputStream.write((Sensor.MOTOR + " ").getBytes());

				// data를 집어넣음
				outputStream.write((sensorDataDto.getSensorDataType() + " " + sensorDataDto.getValue() + " ").getBytes());
				// data 전송
				outputStream.close();

				successfulTransmission = true;
			}
			// 만약 전송에 문제가 생긴경우 (최대 3번 다시 보낸다.)
			catch (IOException e) {
				System.err.println(
					"Failed to send data to server (attempt " + (currentAttempt + 1) + "): " + e.getMessage());
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
