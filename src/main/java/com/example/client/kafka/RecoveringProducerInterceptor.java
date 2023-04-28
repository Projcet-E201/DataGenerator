package com.example.client.kafka;

import com.influxdb.client.InfluxDBClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RecoveringProducerInterceptor implements ProducerInterceptor<String, String> {

    private final InfluxDBClient influxDBClient;

    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        // 데이터 전송 전 처리 작업
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        // 데이터 전송 후 처리 작업
        if(exception != null) {
            log.error("Failed to send message to Kafka topic: " + metadata.topic(), exception);
            // 연결이 끊어졌을 때 데이터를 데이터베이스에 저장합니다.
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void saveToDatabase(RecordMetadata metadata, Exception exception) {
        // 데이터베이스에 저장할 데이터를 생성합니다.

    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
