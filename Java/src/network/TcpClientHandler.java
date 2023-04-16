package network;

import data.sensor.dto.SensorDataDto;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TcpClientHandler {

	private Socket socket;
	private BufferedOutputStream outputStream;

	public TcpClientHandler(String ip, int port) {
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
			outputStream.write((sensorDataDto.getSensor() + " " + sensorDataDto.getId() + " " + sensorDataDto.getValue() + "\n").getBytes());
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
