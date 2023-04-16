//package com.example.generator.data.image;
//
//import java.awt.Graphics2D;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.OutputStream;
//import java.net.Socket;
//import java.util.Random;
//import javax.imageio.ImageIO;
//
//public class Image {
//
//
//    public static void main(String[] args) throws IOException {
//        Socket socket = new Socket("IP", 1);
//        OutputStream outputStream = socket.getOutputStream();
//
//        while (true) {
//            // 이미지 생성 및 리사이징
//            BufferedImage image = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
//            // 랜덤한 색상으로 이미지를 채움
//            Random random = new Random();
//            for (int y = 0; y < image.getHeight(); y++) {
//                for (int x = 0; x < image.getWidth(); x++) {
//                    int r = random.nextInt(256);
//                    int g = random.nextInt(256);
//                    int b = random.nextInt(256);
//                    int color = (r << 16) | (g << 8) | b;
//                    image.setRGB(x, y, color);
//                }
//            }
//
//            BufferedImage resized = new BufferedImage(2048, 2048, BufferedImage.TYPE_INT_RGB);
//            Graphics2D g = resized.createGraphics();
//            g.drawImage(image, 0, 0, 2048, 2048, null);
//            g.dispose();
//
//            // 이미지를 바이트 배열로 변환
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            ImageIO.write(resized, "jpg", baos);
//            byte[] bytes = baos.toByteArray();
//
//            // 서버로 이미지 전송
//            outputStream.write(bytes);
//
//            // 10분마다 대기
//            try {
////                10분에 한번
////                Thread.sleep(10 * 60 * 1000);
////                1초에 한번
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}