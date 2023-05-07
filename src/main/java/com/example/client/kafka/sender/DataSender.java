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

@Component
@Slf4j
@RequiredArgsConstructor
public class DataSender {

    @Value("${client.name}")
    private String clientName;

    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     *
     * @param topic kafka topic 설정
     * @param dataType ex) MOTOR, AIR ...
     */
    public <T> void sendData(String topic, String dataType,T data) {

        // 데이터 전송시간 ex) 2023-04-17/10:12:34.123
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd/HH:mm:ss");
        String currentTime = LocalDateTime.now().format(formatter);

        ListenableFuture<SendResult<String, String>> future;
        String combinedData = clientName + " " + dataType + " " + data + " " + currentTime;

        if(topic.equals("ANALOG") || topic.equals("MACHINE_STATE")) {
            future = kafkaTemplate.send(topic, combinedData);
        } else {
            future = kafkaTemplate.send(clientName, combinedData);
        }

        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Error while sending message: " + ex.getMessage());
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
