package com.example.client.kafka.sender;

import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataSender {

    @Value("${client.name}")
    private String clientName;

    private final WriteApi writeApi;
    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     *
     * @param topic kafka topic 설정
     * @param dataType ex) MOTOR, AIR ...
     */
    public <T> void sendData(String topic, String dataType,T data) {

        // 데이터 전송시간 ex) 2023-04-17/10:12:34.123
        ZoneId seoulZoneId = ZoneId.of("Asia/Seoul");
        ZonedDateTime seoulTime = LocalDateTime.now().atZone(seoulZoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd/HH:mm:ss");
        String currentTime = seoulTime.format(formatter);

        ListenableFuture<SendResult<String, String>> future;
        String combinedData = clientName + " " + dataType + " " + data + " " + currentTime;

        this.saveData(dataType, data + "", currentTime);

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

    private void saveData(String dataType, String data, String time) {

        String bigName = dataType.replaceAll("[0-9]", "");
        String[] machineData;

        if(dataType.equals("MACHINE_STATE")) {
            machineData = data.split(":");
            dataType = machineData[0].toUpperCase();
            data = machineData[1];
        }

        try {
            Point row = Point
                    .measurement(bigName)     // MACHINE_STATE, ANALOG, IMAGE, MOTOR, VACUUM, ..
                    .addTag("name", dataType)    // MOTOR1, VACUUM1, INT, STRING, DOUBLE ..., ANALOG1, IMAGE1
                    .addTag("generate_time", time)
                    .addField("value", data)
                    .time(Instant.now(), WritePrecision.NS);

            writeApi.writePoint(clientName, "semse", row);

        } catch (NumberFormatException e) {
            log.error("Machine State Failed to parse value {} as a Long. Exception message: {} {}", dataType, data, e.getMessage());
            // 예외 처리 로직 추가
        } catch (Exception e) {
            log.error("Machine State Unexpected error occurred while adding TS data. Exception message: {}", e.getMessage());
            // 예외 처리 로직 추가
        }
    }
}
