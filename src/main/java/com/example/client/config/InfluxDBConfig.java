package com.example.client.config;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxDBConfig {

    @Value("${spring.influx.url}")
    private String url;

    @Value("${spring.influx.user}")
    private String user;

    @Value("${spring.influx.password}")
    private String password;

    @Value("${spring.influx.token}")
    private String token;

    @Bean
    public InfluxDBClient influxDBClient() {
        return InfluxDBClientFactory.create(url, token.toCharArray());
    }

}
