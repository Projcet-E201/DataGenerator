package com.example.client.kafka.sender;

import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataSender {

    @Value("${client.name}")
    private String clientName;

    private AtomicInteger dataCount = new AtomicInteger(0);
    private final WriteApi writeApi;
    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     *
     * @param topic kafka topic 설정
     * @param dataType ex) MOTOR, AIR ...
     */
    public <T> void sendData(String topic, String dataType,T data) {

        // 데이터 전송시간 ex) 2023-04-17/10:12:34.123
        ZonedDateTime seoulTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd/HH:mm:ss");
        String currentTime = seoulTime.format(formatter);

        ListenableFuture<SendResult<String, String>> future;
        String combinedData = clientName + " " + dataType + " " + data + " " + currentTime;

//        this.saveData(dataType, data + "", currentTime);

        future = kafkaTemplate.send(topic, combinedData);

        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Error while sending message: " + ex.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, String> result) {
                dataCount.incrementAndGet();
            }
        });
    }

    @Scheduled(fixedRate = 1000*60)
    public void printDataCount() {
        int count = dataCount.getAndSet(0); // 데이터 수 가져오고 초기화
        log.info("1분 간 전송 데이터 개수 : " + count);
    }

    @Async
    protected void saveData(String dataType, String dataValue, String time) {

        String type = dataType;
        String value = dataValue;
        String bigName = dataType.replaceAll("[0-9]", "");


        if(bigName.equals("MACHINE_STATE")) {
            String[] machineData = dataValue.split(":");
            type = machineData[0].toUpperCase();
            value = machineData[1];
        }

        try {
            Point row = Point
                    .measurement(bigName)
                    .addTag("name", type)
                    .addTag("generate_time", time)
                    .time(Instant.now(), WritePrecision.NS);

            if(type.startsWith("STRING")) {
                // STRING
                row.addField("value_str", value);
            } else if (type.startsWith("DOUBLE") || type.startsWith("AIR_OUT_MPA")) {
                // DOUBLE
                row.addField("value_double", Double.parseDouble(value));
            } else {
                // INT
                row.addField("value", Integer.parseInt(value));
            }

            writeApi.writePoint(clientName, "semse", row);

        } catch (NumberFormatException e) {
            log.error("Machine State Failed to parse value {} as a Long. Exception message: {} {}", type, value, e.getMessage());
            // 예외 처리 로직 추가
        } catch (Exception e) {
            log.error("Machine State Unexpected error occurred while adding TS data. Exception message: {}", e.getMessage());
            // 예외 처리 로직 추가
        }
    }
}
