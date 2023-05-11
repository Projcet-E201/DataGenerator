package com.example.client.kafka.sender;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * 1MB 보다 큰 데이터를 쪼개서 전송합니다.
 * (ex. IMAGE)
 */

@Component
@Slf4j
@RequiredArgsConstructor
public class ChunkDataSender {

    @Value("${client.name}")
    private String clientName;

    private final KafkaTemplate<String, String> kafkaTemplate;

    protected List<String> encode(String message) {
        UUID uuid = UUID.randomUUID();

        List<String> chunks = new ArrayList<>();
        int chunkSize = 1024 * 512;  // 데이터 분할할 최대 길이 설정

        for (int i = 0; i < message.length(); i += chunkSize) {
            int end = Math.min(message.length(), i + chunkSize);
            chunks.add(message.substring(i, end) + " " + uuid);
        }

        return chunks;
    }

    /**
     * @param topic kafka topic 설정
     * @param dataType ex) MOTOR, AIR ...
     */
    public <T> void sendData(String topic, String dataType,T data) {

        // 데이터 전송시간 ex) 2023-04-17/10:12:34.123
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd/HH:mm:ss");
        String currentTime = LocalDateTime.now().format(formatter);

        String dataValue = "" + data;
        List<String> chunks = encode(dataValue);


        for (String chunk : chunks) {
            String combinedData = clientName + " " + dataType + " " + chunk + " " + currentTime;

            // 머신별로 토픽을 나누고, 내부 파티션에서는 라운드로빈 방식으로 저장
            ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, combinedData);
            future.addCallback(new ListenableFutureCallback<>() {
                @Override
                public void onFailure(Throwable ex) {
                    System.err.println("Error while sending message: " + ex.getMessage());
                }

                @Override
                public void onSuccess(SendResult<String, String> result) {
                    RecordMetadata metadata = result.getRecordMetadata();
                    System.out.println("Message sent to partition " +  metadata.topic() + " - " + metadata.partition() +
                            " with offset " + metadata.offset() + " at " + metadata.timestamp());
                }
            });
        }
    }
}
