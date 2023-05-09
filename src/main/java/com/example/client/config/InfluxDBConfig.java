package com.example.client.config;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.WriteOptions;
import com.influxdb.client.write.events.BackpressureEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxDBConfig {

    @Value("${spring.influx.url}")
    private String url;

    @Value("${spring.influx.username}")
    private String username;

    @Value("${spring.influx.password}")
    private String password;

    @Value("${spring.influx.token}")
    private String token;


    @Bean
    public InfluxDBClient influxDBClient() {
        return InfluxDBClientFactory.create(url, token.toCharArray());
    }

    @Bean
    public WriteApi writeApi(InfluxDBClient influxDBClient) {
        WriteOptions options = WriteOptions.builder()
                .bufferLimit(100_000_000)
                .build();

        WriteApi writeApi = influxDBClient.makeWriteApi(options);
        writeApi.listenEvents(BackpressureEvent.class, event -> {
            //  BackpressureEvent 처리 로직
        });

        return writeApi;
    }
}
