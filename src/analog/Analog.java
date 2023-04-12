package analog;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import config.Config;

public class Analog {
	// 서버 정보
	private static final String ip = Config.LOCAL_IP;
	private static final int port = Config.ANALOG_PORT;

	// 데이터 정보
	private static final int sendDataSize = 200; // 해당 개수가 생성되면 데이터를 보낸다.
	private static final int retries= 3; // 재시도 횟수
	private static final int FILE_SIZE = 400 * 1024; // 400kb
	private static final String OUTPUT_DIRECTORY = "server_directory/"; // 저장위치
	private static final DateTimeFormatter FILE_NAME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm"); // 파일포맷터


	public static void main(String[] args){

		// 아날로그 데이터를 1분마다 생성
		ScheduledExecutorService analog = Executors.newSingleThreadScheduledExecutor();
		analog.scheduleAtFixedRate(() -> {
			try {
				generateAndCompressFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}, 0, 5, TimeUnit.SECONDS);


		// 서버에 1시간마다 보낸다.
		ScheduledExecutorService timer = Executors.newSingleThreadScheduledExecutor();
		timer.scheduleAtFixedRate(() -> {
			try {
				transmitFilesToServer();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}, 5, 10, TimeUnit.SECONDS);
	}

	/**
	 * 숫자로 구성된 바이너리 형식, 400kb의 zip 더미파일을 생성한다.
	 */
	private static void generateAndCompressFile() throws IOException {

		// 현재 날짜와 시간은 yyyyMMddHHmm 형식으로 만든다.
		String fileName = LocalDateTime.now().format(FILE_NAME_FORMATTER);

		// Files.createTempFile 메서드를 이용해서 임시 바이너리 파일을 만든다.
		Path tempFile = Files.createTempFile(fileName, ".bin");

		// 임의의 400KB 만큼의 이진 데이터 임시파일을 만들기 위해서 사용
		try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(tempFile.toFile()))) {
			byte[] randomData = new byte[FILE_SIZE]; // 파일 크기만큼의 Buffer 크기 생성
			new java.util.Random().nextBytes(randomData); // 임의의 바이트로 채워준다.
			outputStream.write(randomData); // 임의의 데이터를 파일에 넣어줌
		}

		// 임시 바이너리 파일을 ZIP 파일로 압축하기 위해 ZipOutputStream을 생성한다.
		// 바이너리 파일에 대해 새로운 ZipEntry가 생성되고 ZipOutputStream에 추가된다.
		try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(fileName + ".zip"))) {
			zipOutputStream.putNextEntry(new ZipEntry(fileName + ".bin"));
			Files.copy(tempFile, zipOutputStream);
			zipOutputStream.closeEntry();
		}
		// 임시 바이너리 파일을 삭제한다.
		// Files.delete(tempFile);
	}

	/**
	 * ZIP파일을 출력 디렉토리로 이동하여 서버로 전송하는 역할
	 * @throws IOException
	 */
	private static void transmitFilesToServer() throws IOException {
		// 확장자가 .zip인 파일을 필터링하기 위해서 사용(.zip으로 끝나는지 확인)
		DirectoryStream.Filter<Path> filter = entry -> String.valueOf(entry).endsWith(".zip");

		// DirectoryStream<Path> 개체가 생성된다. (제공된 필터와 일치)
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get("."), filter)) {

			// 메서드는 for-each루프를 이용하여 현재 디렉토리에서 필터링된 파일을 반복한다.
			for (Path path : directoryStream) {
				// 각 파일에대해, OUTPUT_DIRECTORY 와 파일이름을 결합하여 경로 생성, Paths.get은 경로 구성에 사용됨
				Path destinationPath = Paths.get(OUTPUT_DIRECTORY, path.getFileName().toString());
				// StandardCopyOption.REPLACE_EXISTING옵션을 사용하여 파일을 대상 경로로 이동
				Files.move(path, destinationPath, StandardCopyOption.REPLACE_EXISTING);

				sendDataToServer(destinationPath.toString());
			}
		}
	}

	private static void sendDataToServer(String zipFileName) {
		int maxRetries = retries; // number of retries
		int currentAttempt = 0;
		boolean successfulTransmission = false;
		Socket socket = null;

		while (!successfulTransmission && currentAttempt < maxRetries) {
			try {
				// Socket 연결
				socket = new Socket(ip, port);

				// 데이터를 전송할 outputStream 생성
				try (OutputStream outputStream = socket.getOutputStream();
					 FileInputStream fileInputStream = new FileInputStream(zipFileName)) {

					byte[] buffer = new byte[1024];
					int bytesRead;

					// data를 집어넣음
					while ((bytesRead = fileInputStream.read(buffer)) != -1) {
						outputStream.write(buffer, 0, bytesRead);
					}

					// data 전송
					outputStream.close();
					successfulTransmission = true;
				}
			} catch (IOException e) {
				System.err.println("Failed to send data to server (attempt " + (currentAttempt + 1) + "): " + e.getMessage());
				currentAttempt++;
			} finally {
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
