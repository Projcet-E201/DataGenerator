package sensor;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import config.Config;
import config.Sensor;

public class TcpClient {

	private Socket socket;
	private BufferedOutputStream outputStream;

	public TcpClient(String ip, int port) {
		try {
			socket = new Socket(ip, port);
			outputStream = new BufferedOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Data를 Socket으로 전송하는 함수
	 */
	public boolean sendData(SensorDataDto sensorDataDto) {
		try {
			// insert data
			outputStream.write((Sensor.MOTOR + " ").getBytes());
			outputStream.write((sensorDataDto.getSensorDataType() + " " + sensorDataDto.getValue() + " ").getBytes());
			outputStream.flush(); // Flush the buffer to send the data
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void close() {
		try {
			outputStream.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
