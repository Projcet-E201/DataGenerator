import config.Config;

import java.io.*;
import java.net.Socket;

public class Image {

    private static final String ip = Config.IP;
    private static final int port = Config.IMAGE_PORT;
    public static void main(String[] args) {
        try {
            // 소켓 연결

            Socket socket = new Socket(ip, port);

            // 이미지 파일 읽기
            File file = new File("server_directory/");
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();

            // 서버로 데이터 전송
            OutputStream out = socket.getOutputStream();
            out.write(buffer);
            out.flush();

            // 소켓 종료
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
